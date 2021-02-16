package net.verany.api.gamemode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.gamemode.countdown.AbstractCountdown;

@AllArgsConstructor
@Getter
public enum GameState {

    WAITING(21, false, null),
    LOBBY(61, false, null),
    NO_MOVE(11, false, null),
    IN_GAME(3601, true, null),
    DEATH_MATCH_STARTING(61, true, null),
    DEATH_MATCH(301, true, null),
    RESTART(21, false, null),
    PROTECTION(31, true, null);

    @Setter
    private int seconds;
    @Setter
    private boolean inGame;
    @Setter
    private AbstractCountdown countdown;


}
