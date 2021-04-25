package net.verany.executor.commands;

import net.verany.api.Verany;
import net.verany.api.tablist.TabListObject;
import net.verany.executor.CoreExecutor;
import net.verany.executor.event.VeranyReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender.hasPermission("verany.command.reload"))) {
            return false;
        }

        VeranyReloadEvent.ReloadType reloadType = null;
        String file = null;
        boolean success = true;

        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("messages")) {
                    reloadType = VeranyReloadEvent.ReloadType.MESSAGES;
                    try {
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading messages§8...");
                        Verany.reloadMessages(CoreExecutor.INSTANCE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading messages! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading " + Verany.asDecimal(Verany.MESSAGES.size()) + " messages success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading messages failed!");
                    }
                } else if (args[0].equalsIgnoreCase("setup")) {
                    reloadType = VeranyReloadEvent.ReloadType.USER;
                    try {
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading setup§8...");
                        Verany.reloadSetup();
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading setup! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading setup success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading setup failed!");
                    }
                } else if (args[0].equalsIgnoreCase("users")) {
                    reloadType = VeranyReloadEvent.ReloadType.USER;
                    try {
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading users§8...");
                        //Verany.reloadUser();
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading users! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading users success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading users failed!");
                    }
                } else if (args[0].equalsIgnoreCase("permissions")) {
                    reloadType = VeranyReloadEvent.ReloadType.PERMISSIONS;
                    try {
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading permissions§8...");
                        Verany.loadPermissionGroups(CoreExecutor.INSTANCE);
                        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> new TabListObject().setTabList(onlinePlayer));
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading permissions! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading permissions success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading permissions failed!");
                    }
                } else if (args[0].equalsIgnoreCase("plugins")) {
                    reloadType = VeranyReloadEvent.ReloadType.PLUGINS;
                    try {
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Disabling plugins§8...");
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            if (!plugin.getName().contains("CloudNetAPI"))
                                Bukkit.getPluginManager().disablePlugin(plugin);
                        }
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Load plugins§8...");
                        File pluginFolder = new File("plugins");
                        for (Plugin plugin : Bukkit.getPluginManager().loadPlugins(pluginFolder)) {
                            Bukkit.getPluginManager().enablePlugin(plugin);
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Enabling plugin§8: §b" + plugin.getName() + "§8...");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading plugins! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading plugins success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading plugins failed!");
                    }
                } else if (args[0].equalsIgnoreCase("proxy")) {
                    reloadType = VeranyReloadEvent.ReloadType.PROXY_ALL;
                    sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading proxy§8...");

                } else if (args[0].equalsIgnoreCase("file")) {
                    sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cSyntax: /reload file <file-name>");
                    sender.sendMessage(" ");
                } else if (args[0].equalsIgnoreCase("bukkit")) {
                    reloadType = VeranyReloadEvent.ReloadType.BUKKIT;
                    try {
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading Bukkit§8...");
                        Bukkit.reload();
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading Bukkit! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading Bukkit success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading Bukkit failed!");
                    }
                } else if (args[0].equalsIgnoreCase("locations")) {
                    reloadType = VeranyReloadEvent.ReloadType.LOCATIONS;
                    try {
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading locations§8...");
                        //Verany.loadLocations();
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading locations! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading locations success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading locations failed!");
                    }
                } else {
                    sendHelp(sender);
                    return false;
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("file")) {
                    reloadType = VeranyReloadEvent.ReloadType.FILE;
                    file = args[1];
                    sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading file§8: §b" + file + "§8...");
                } else if (args[0].equalsIgnoreCase("proxy")) {
                    switch (args[1]) {
                        case "messages":
                            reloadType = VeranyReloadEvent.ReloadType.PROXY_MESSAGES;
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading proxy messages§8...");
                            break;
                        case "users":
                            reloadType = VeranyReloadEvent.ReloadType.PROXY_USERS;
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading proxy users§8...");

                            break;
                        case "permissions":
                            reloadType = VeranyReloadEvent.ReloadType.PROXY_PERMISSIONS;
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reloading proxy permissions§8...");

                            break;
                    }
                } else if (args[0].equalsIgnoreCase("plugins")) {
                    reloadType = VeranyReloadEvent.ReloadType.PLUGIN;
                    String pluginName = args[1];
                    try {
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
                        Bukkit.getPluginManager().disablePlugin(plugin);
                        Plugin newPlugin = Bukkit.getPluginManager().loadPlugin(new File("plugins/" + pluginName + ".jar"));
                        Bukkit.getPluginManager().enablePlugin(newPlugin);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§4An error occurred while reloading plugin (" + pluginName + ")! [" + e + "]");
                        success = false;
                    } finally {
                        if (success)
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§aReloading plugin (" + pluginName + ") success!");
                        else
                            sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§cReloading plugin (" + pluginName + ") failed!");
                    }
                } else {
                    sendHelp(sender);
                    return false;
                }
                break;
            default:
                sendHelp(sender);
                return false;
        }

        Bukkit.getPluginManager().callEvent(new VeranyReloadEvent(success, reloadType, file));

        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(Verany.getPrefix("VeranySpigot") + "§7Reload commands§8:");
        sender.sendMessage(" ");
        sender.sendMessage(" §8» §b/reload bukkit");
        sender.sendMessage(" §8» §b/reload messages");
        sender.sendMessage(" §8» §b/reload users");
        sender.sendMessage(" §8» §b/reload worlds");
        sender.sendMessage(" §8» §b/reload permissions");
        //sender.sendMessage(" §8» §b/reload plugins (<name>)");
        sender.sendMessage(" §8» §b/reload settings");
        sender.sendMessage(" §8» §b/reload achievements");
        sender.sendMessage(" §8» §b/reload manager");
        sender.sendMessage(" §8» §b/reload locations");
        sender.sendMessage(" §8» §b/reload proxy (<messages, users, permissions>)");
        sender.sendMessage(" §8» §b/reload file <file-name>");
        sender.sendMessage(" ");
    }

}
