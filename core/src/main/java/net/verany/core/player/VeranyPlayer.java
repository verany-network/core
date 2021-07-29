package net.verany.core.player;

import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IVeranyPlayer;

public abstract class VeranyPlayer extends DatabaseLoader implements IVeranyPlayer {

    public VeranyPlayer(VeranyProject project, String collection) {
        super(project, collection);
    }
}
