package net.verany.api;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.verany.api.actionbar.ActionbarTask;
import net.verany.api.config.IngameConfig;
import net.verany.api.listener.ChatListener;
import net.verany.api.listener.PlayerJoinListener;
import net.verany.api.listener.PlayerQuitListener;
import net.verany.api.listener.ProtectionListener;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.ProfileObject;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@VeranyModule(name = "Core", prefix = "Core", version = "2022.4.1", authors = {"tylix"})
public class Core extends VeranyPlugin {

    public static Core INSTANCE;

    public Core() {
        INSTANCE = this;
    }

    static {
        try {
            Class.forName("net.verany.api.language.VeranyLanguage");
            Class.forName("net.verany.api.player.permission.group.PermissionGroup");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        Verany.loadModule(this, this::init);
        //Verany.connectToWebsocket();
    }

    @Override
    public void init() {
        //Verany.MESSENGER.auth(this);

        Verany.EVENT_REGISTRY.setPlugin(this);
        Verany.PROFILE_OBJECT = new ProfileObject();

        IngameConfig.TAB_LIST_FORMAT.setValue("{0}{1} §8▏ §7");

        loadPermissionGroups();
        Verany.reloadMessages(this);

        for (Document players : getConnection().getCollection("players").find()) {
            IPlayerInfo playerInfo = new PlayerInfo(this, players.getString("name"));
            playerInfo.load(UUID.fromString(players.getString("uuid")));
            Verany.setPlayer(IPlayerInfo.class, playerInfo);
            Verany.PROFILE_OBJECT.setPlayer(IPlayerInfo.class, playerInfo);
        }

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.OFF);

        Verany.addTask(new ActionbarTask(400, this));

        initListeners();

    }

    private void initListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new ProtectionListener(this), this);
        pluginManager.registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        Verany.shutdown(this);
    }

    private void loadPermissionGroups() {
        MongoCollection<Document> collection = getConnection().getCollection("groups");
        if(collection == null) {

            return;
        }
        for (AbstractPermissionGroup value : PermissionGroup.VALUES)
            if (collection.find(Filters.eq("name", value.getName())).first() == null)
                collection.insertOne(Verany.GSON.fromJson(Verany.GSON.toJson(value), Document.class));
        for (Document document : collection.find()) {
            AbstractPermissionGroup permissionGroup = Verany.GSON.fromJson(document.toJson(), PermissionGroup.class);
            if (PermissionGroup.getGroupByName(permissionGroup.getName()) == null)
                PermissionGroup.VALUES.add(permissionGroup);
        }
        for (AbstractPermissionGroup value : PermissionGroup.VALUES)
            for (AbstractPermissionGroup child : value.getChildren())
                value.getPermissions().addAll(child.getPermissions());
    }

}
