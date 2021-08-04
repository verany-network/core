package net.verany.api.player;

import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;

public abstract class VeranyPlayer extends DatabaseLoader implements IVeranyPlayer {

    public VeranyPlayer(VeranyProject project, String collection) {
        super(project, collection);
    }
}
