package net.verany.api;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import net.verany.api.event.EventManager;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.LanguageData;
import net.verany.api.listener.PlayerJoinListener;
import net.verany.api.message.MessageData;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.ProfileObject;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@VeranyModule(name = "Core", prefix = "Core", version = "2021.7.7", authors = {"tylix", "xLikeAlex", "Gamingcode"})
public class Core extends VeranyPlugin {

    public static Core INSTANCE;

    public Core() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        Verany.loadModule(this, this::init);
        Verany.connectToWebsocket();
    }

    @Override
    public void init() {
        Verany.MESSENGER.auth(this);

        Verany.EVENT_REGISTRY.setPlugin(this);
        Verany.PROFILE_OBJECT = new ProfileObject();

        loadPermissionGroups();
        Verany.reloadMessages(this);

        for (Document players : getConnection().getCollection("players").find()) {
            IPlayerInfo playerInfo = new PlayerInfo(this, players.getString("name"));
            playerInfo.load(UUID.fromString(players.getString("uuid")));
            Verany.PROFILE_OBJECT.setPlayer(IPlayerInfo.class, playerInfo);
        }

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.OFF);

        initListeners();

    }

    private void initListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        Verany.shutdown(this);
    }

    private void loadPermissionGroups() {
        AbstractPermissionGroup.VALUES.clear();
        MongoCollection<Document> collection = getConnection().getCollection("rank", "groups");
        for (AbstractPermissionGroup value : AbstractPermissionGroup.VALUES)
            if (collection.find(Filters.eq("name", value.getName())).first() == null)
                collection.insertOne(Verany.GSON.fromJson(Verany.GSON.toJson(value), Document.class));
        for (Document document : collection.find()) {
            AbstractPermissionGroup permissionGroup = Verany.GSON.fromJson(document.toJson(), PermissionGroup.class);
            if (AbstractPermissionGroup.getGroupByName(permissionGroup.getName()) == null)
                AbstractPermissionGroup.VALUES.add(permissionGroup);
        }
        for (AbstractPermissionGroup value : AbstractPermissionGroup.VALUES)
            for (AbstractPermissionGroup child : value.getChildren())
                value.getPermissions().addAll(child.getPermissions());
    }

    public static void updatePermissionGroup(VeranyProject project, AbstractPermissionGroup group) {
        project.getConnection().getCollection("rank", "groups").updateOne(new BasicDBObject("name", group.getName()), new BasicDBObject("$set", new BasicDBObject("permissions", group.getPermissions())));
        project.getConnection().getCollection("rank", "groups").updateOne(new BasicDBObject("name", group.getName()), new BasicDBObject("$set", new BasicDBObject("children", group.getStringChildren())));
    }


}
