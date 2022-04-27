package net.verany.api.player;

import lombok.Getter;
import net.verany.api.language.VeranyLanguage;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.PermissionObject;
import net.verany.api.prefix.PrefixPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter
public abstract class VeranyPlayer extends DatabaseLoader implements IVeranyPlayer {

    private UUID uniqueId;
    private final String name;

    private IPermissionObject permissionObject;

    public VeranyPlayer(VeranyPlugin plugin, String name) {
        super(plugin, "players");
        this.name = name;
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        load(new LoadInfo<>("user", PlayerEntry.class, new PlayerEntry(uniqueId, name, VeranyLanguage.ENGLISH.getName(), PrefixPattern.BLUE.getKey(), 0, 0, 0, 0L, 0, 0, 0, new ArrayList<>(), new HashMap<>(), new ArrayList<>())));

        permissionObject = new PermissionObject(getProject());
        permissionObject.load(key);
    }

    @Override
    public void update() {
        save("user");

        permissionObject.update();
    }
}
