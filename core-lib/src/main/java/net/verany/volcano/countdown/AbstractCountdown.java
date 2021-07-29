package net.verany.volcano.countdown;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractCountdown {

    private final String key;
    private int seconds;
    private final Integer[] run;
    private int count;
    private boolean running = false;

    protected AbstractCountdown(String key, int seconds, Integer... run) {
        this.key = key;
        this.seconds = seconds;
        this.run = run;
        this.count = seconds;
    }

    public abstract void run();

    public void finished() {
    }

    public void onCount() {
    }

    public void start() {
        running = true;
    }

    public void pause() {
        running = false;
    }

    public void stop() {
        running = false;
        count = seconds;
    }

}
