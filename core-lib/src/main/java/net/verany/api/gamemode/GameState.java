package net.verany.api.gamemode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum GameState {

    WAITING(21, false),
    LOBBY(61, false),
    NO_MOVE(11, false),
    IN_GAME(3601, true),
    DEATH_MATCH_STARTING(61, true),
    DEATH_MATCH(301, true),
    RESTART(21, false),
    PROTECTION(31, true);

    @Setter
    private int seconds;
    @Setter
    private boolean inGame;


}
