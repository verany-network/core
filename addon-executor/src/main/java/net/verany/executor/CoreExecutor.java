package net.verany.executor;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.actionbar.ActionbarTask;
import net.verany.api.bossbar.BossBarTask;
import net.verany.api.chat.task.ChatTask;
import net.verany.api.database.Database;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.GameModeWrapper;
import net.verany.api.gamemode.countdown.task.CountdownTask;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.afk.task.AfkTask;
import net.verany.api.player.friend.settings.FriendSetting;
import net.verany.api.player.onlinetime.OnlineTimeTask;
import net.verany.executor.commands.AFKCommand;
import net.verany.executor.commands.ChatClearCommand;
import net.verany.executor.commands.CreditsCommand;
import net.verany.executor.commands.GlobalRankCommand;
import net.verany.executor.commands.LanguageCommand;
import net.verany.executor.commands.OnlineTimeCommand;
import net.verany.executor.commands.PlayTimeCommand;
import net.verany.executor.commands.ReloadCommand;
import net.verany.executor.commands.SupportCommand;
import net.verany.executor.commands.VerifyCommand;
import net.verany.executor.listener.ChatListener;
import net.verany.executor.listener.PlayerJoinListener;
import net.verany.executor.listener.PlayerQuitListener;
import net.verany.executor.listener.ProtectionListener;
import org.bson.Document;
import org.bukkit.Bukkit;

@Getter
@VeranyModule(name = "CoreExecutor", prefix = "Core", version = "0.1", authors = {"tylix"}, user = "tylix", host = "159.69.63.105", password = "RxNqA18HB56SS7GW", databases = {"network", "bots", "friends", "rank", "verification", "socket"})
public class CoreExecutor extends VeranyProject {

    public static CoreExecutor INSTANCE;

    private final File file = new File("plugins//Core//database.json");

    @Override
    public void onEnable() {
        initJsonConfig();

        Verany.loadModule(this, loadJsonConfig());

        INSTANCE = this;
        init();
    }

    private void initJsonConfig() {
        if (file.exists()) return;
        Gson gson = new Gson();

        Database database = new Database("user", "password", "127.0.0.1", new String[]{"database1", "database2"});
        try (FileWriter fileWriter = new FileWriter(file)) {
            file.createNewFile();
            fileWriter.write(gson.toJson(database));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Database loadJsonConfig() {
        if (!file.exists()) initJsonConfig();

        Gson gson = new Gson();
        try {
            return gson.fromJson(new FileReader(file), Database.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDisable() {
        Verany.shutdown = true;
        Verany.MESSENGER.close();
        for (IPlayerInfo playerInfo : Verany.PROFILE_OBJECT.getRegisteredPlayers())
            playerInfo.update();
        getConnection().getCollection("socket", "sockets").deleteOne(Filters.eq("key", Verany.KEY));
        Verany.shutdown(this);
    }

    @Override
    public void init() {
        super.init();
        new ChatListener(this);
        new ProtectionListener(this);
        new PlayerJoinListener(this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);

        this.getCommand("reload").setExecutor(new ReloadCommand());
        this.getCommand("verify").setExecutor(new VerifyCommand());
        this.getCommand("verify").setTabCompleter(new VerifyCommand());
        this.getCommand("afk").setExecutor(new AFKCommand());
        this.getCommand("language").setExecutor(new LanguageCommand());
        this.getCommand("onlinetime").setExecutor(new OnlineTimeCommand());
        this.getCommand("playtime").setExecutor(new PlayTimeCommand());
        this.getCommand("credits").setExecutor(new CreditsCommand());
        this.getCommand("support").setExecutor(new SupportCommand());
        this.getCommand("support").setTabCompleter(new SupportCommand());
        /*this.getCommand("rank").setExecutor(new RankCommand());
        this.getCommand("rank").setTabCompleter(new RankCommand());*/
        this.getCommand("globalrank").setExecutor(new GlobalRankCommand());
        this.getCommand("globalrank").setTabCompleter(new GlobalRankCommand());
        this.getCommand("chatclear").setExecutor(new ChatClearCommand());

        FriendSetting.REQUESTS.getCategory();
        Verany.loadPermissionGroups(this);

        for (Document document : getConnection().getCollection("players").find()) {
            IPlayerInfo playerInfo = new PlayerInfo(this, document.getString("name"));
            playerInfo.load(UUID.fromString(document.getString("uuid")));
            Verany.PROFILE_OBJECT.getRegisteredPlayers().add(playerInfo);
        }

        for (Document document : getConnection().getCollection("games").find()) {
            AbstractGameMode gameMode = Verany.GSON.fromJson(document.toJson(), GameModeWrapper.class);
            GameModeWrapper.VALUES.add(gameMode);
        }
        if (GameModeWrapper.VALUES.isEmpty()) {
            getConnection().getCollection("games").insertOne(Verany.GSON.fromJson(Verany.GSON.toJson(new GameModeWrapper("FlagWars", new String[]{"FW-2x1"})), Document.class));
        }

        Verany.reloadMessages(this);
        Verany.reportObject.load();

        String key = Verany.KEY;
        getConnection().getCollection("socket", "sockets").insertOne(new Document().append("key", key).append("type", "core"));
        Verany.MESSENGER.auth(this, key);

        Verany.addTask(new ChatTask(1000, this), new BossBarTask(1, this), new CountdownTask(1000), new ActionbarTask(100, this), new OnlineTimeTask(1000), new AfkTask(800, this));
    }
}
