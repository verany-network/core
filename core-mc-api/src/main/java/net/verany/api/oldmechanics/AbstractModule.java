package net.verany.api.oldmechanics;

import lombok.Getter;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import org.bukkit.event.Listener;

@Getter
public abstract class AbstractModule implements Listener {

    private final String name;

    private boolean enabled;
    private VeranyPlugin project;

    public AbstractModule() {
        this.name = getClass().getSimpleName();
    }

    public void load() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled, VeranyPlugin project) {
        this.enabled = enabled;
        this.project = project;
        updateState(enabled);
    }

    public void updateState(boolean newState) {
    }

    public String getName() {
        return name;
    }
}
