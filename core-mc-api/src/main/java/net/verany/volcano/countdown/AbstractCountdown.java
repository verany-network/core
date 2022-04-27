package net.verany.volcano.countdown;

import lombok.Getter;
import lombok.Setter;
import net.verany.volcano.round.AbstractVolcanoRound;

@Getter
@Setter
public abstract class AbstractCountdown {

    private final AbstractVolcanoRound round;
    private final String key;
    private int seconds;
    private final Integer[] run;
    private int count;
    private boolean running = false;

    private Runnable onFinish;

    protected AbstractCountdown(AbstractVolcanoRound round, String key, int seconds, Integer... run) {
        this.round = round;
        this.key = key;
        this.seconds = seconds;
        this.run = run;
        this.count = seconds;
    }

    public abstract void run();

    public void finished() {
        onFinish.run();
    }

    public void onCount() {
    }

    public AbstractCountdown start() {
        running = true;
        return this;
    }

    public AbstractCountdown onFinish(Runnable run) {
        onFinish = run;
        return this;
    }

    public void pause() {
        running = false;
    }

    public void stop() {
        running = false;
        count = seconds;
    }

}
