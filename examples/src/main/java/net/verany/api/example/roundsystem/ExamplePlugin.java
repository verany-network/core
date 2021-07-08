package net.verany.api.example.roundsystem;

import net.verany.api.Verany;
import net.verany.api.example.normal.commands.ExampleCommand;
import net.verany.api.example.normal.listener.PlayerJoinListener;
import net.verany.api.example.normal.listener.PlayerQuitListener;
import net.verany.api.example.roundsystem.game.AbstractExampleManager;
import net.verany.api.example.roundsystem.game.ExampleManager;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.volcano.VeranyServer;
import net.verany.volcano.round.AbstractVolcanoRound;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

@VeranyModule(name = "Example", prefix = "Example", version = "2021.6.1", authors = {"tylix"})
public class ExamplePlugin extends VeranyProject {

    public static ExamplePlugin INSTANCE;

    public ExamplePlugin() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        Verany.loadModule(this);

        init();
    }

    @Override
    public void init() {

        VeranyServer.setGameManager(abstractVolcanoRounds -> {
            for (AbstractVolcanoRound round : abstractVolcanoRounds) {
                AbstractExampleManager gameManager = new ExampleManager(round);
                round.setGameManager(AbstractExampleManager.class, gameManager);
                gameManager.update();
            }
        });

        initCommands();
        initListener();
    }

    private void initCommands() {
        new ExampleCommand(this);
    }

    private void initListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
