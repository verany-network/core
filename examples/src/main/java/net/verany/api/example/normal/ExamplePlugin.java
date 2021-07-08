package net.verany.api.example.normal;

import net.verany.api.Verany;
import net.verany.api.example.normal.commands.ExampleCommand;
import net.verany.api.example.normal.listener.PlayerJoinListener;
import net.verany.api.example.normal.listener.PlayerQuitListener;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
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
