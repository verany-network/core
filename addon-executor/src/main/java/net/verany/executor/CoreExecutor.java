package net.verany.executor;

import net.verany.api.Verany;
import net.verany.api.actionbar.ActionbarTask;
import net.verany.api.chat.task.ChatTask;
import net.verany.api.config.IngameConfig;
import net.verany.api.logger.VeranyLog;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.afk.task.AfkTask;
import net.verany.api.player.friend.settings.FriendSetting;
import net.verany.api.player.onlinetime.OnlineTimeTask;
import net.verany.executor.commands.*;
import net.verany.executor.listener.*;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@VeranyModule(name = "CoreExecutor", prefix = "CoreExecutor", version = "0.1", authors = {"tylix"}, user = "tylix", host = "159.69.63.105", password = "", databases = {"network", "bots", "friends", "rank", "verification"})
public class CoreExecutor extends VeranyProject {

    public static CoreExecutor INSTANCE;

    @Override
    public void onEnable() {
        Verany.loadModule(this);
        INSTANCE = this;

        init();
    }

    @Override
    public void onDisable() {
        for (IPlayerInfo playerInfo : Verany.PROFILE_OBJECT.getRegisteredPlayers())
            playerInfo.update();
        getConnection().disconnect();
        Verany.shutdown();
    }

    @Override
    public void init() {
        super.init();
        new ChatListener(this);
        new ProtectionListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

        this.getCommand("reload").setExecutor(new ReloadCommand());
        this.getCommand("verify").setExecutor(new VerifyCommand());
        this.getCommand("verify").setTabCompleter(new VerifyCommand());
        this.getCommand("prefix").setExecutor(new PrefixCommand());
        this.getCommand("afk").setExecutor(new AFKCommand());
        this.getCommand("language").setExecutor(new LanguageCommand());
        this.getCommand("onlinetime").setExecutor(new OnlineTimeCommand());
        this.getCommand("playtime").setExecutor(new PlayTimeCommand());
        this.getCommand("credits").setExecutor(new CreditsCommand());
        this.getCommand("support").setExecutor(new SupportCommand());

        FriendSetting.REQUESTS.getCategory();
        Verany.loadPermissionGroups(this);

        for (Document document : getConnection().getCollection("players").find()) {
            IPlayerInfo playerInfo = new PlayerInfo(this, document.getString("name"));
            playerInfo.load(UUID.fromString(document.getString("uuid")));
            Verany.PROFILE_OBJECT.getRegisteredPlayers().add(playerInfo);

            VeranyLog.debug(getModule(), playerInfo.getUniqueId().toString() + " loaded");
        }

        Verany.reloadMessages(this);

        Verany.addTask(new ChatTask(1000, this), new ActionbarTask(100, this), new OnlineTimeTask(1000), new AfkTask(800, this));
    }
}
