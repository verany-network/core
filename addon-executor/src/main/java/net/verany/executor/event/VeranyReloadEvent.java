package net.verany.executor.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class VeranyReloadEvent extends Event {

    private final boolean success;
    private final ReloadType reloadType;
    private final String file;

    public VeranyReloadEvent(boolean success, ReloadType reloadType, String file) {
        this.success = success;
        this.reloadType = reloadType;
        this.file = file;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public enum ReloadType {
        MESSAGES,
        FILE,
        USER,
        PERMISSIONS,
        PROXY_MESSAGES,
        PROXY_USERS,
        PROXY_PERMISSIONS,
        PROXY_ALL,
        PLUGINS,
        PLUGIN,
        BUKKIT,
        WORLDS,
        MANAGER,
        LOCATIONS
    }
}
