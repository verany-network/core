package net.verany.api.command.executor;

import net.verany.api.player.IPlayerInfo;

public interface VeranyCommandExecutor {

    void onExecute(IPlayerInfo sender, String[] args);

}
