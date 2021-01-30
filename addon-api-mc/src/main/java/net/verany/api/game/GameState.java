package net.verany.api.game;

public enum GameState {

    WAITING(21, false),
    LOBBY(61, false),
    NO_MOVE(11, false),
    IN_GAME(3601, true),
    DEATH_MATCH_STARTING(61, true),
    DEATH_MATCH(301, true),
    RESTART(21, false),
    PROTECTION(31, true);

    private int seconds;
    private boolean inGame;

    GameState(int seconds, boolean inGame) {
        this.seconds = seconds;
        this.inGame = inGame;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isInGame() {
        return this.inGame;
    }


}
