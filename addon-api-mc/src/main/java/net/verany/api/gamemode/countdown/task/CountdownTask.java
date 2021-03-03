package net.verany.api.gamemode.countdown.task;

import net.verany.api.AbstractVerany;
import net.verany.api.gamemode.countdown.AbstractCountdown;
import net.verany.api.task.AbstractTask;

import java.util.Arrays;

public class CountdownTask extends AbstractTask {

    public CountdownTask(long waitTime) {
        super(waitTime);
    }

    @Override
    public void run() {
        for (AbstractCountdown countdown : AbstractVerany.COUNTDOWNS) {
            if (!countdown.isRunning()) continue;
            countdown.setCount(countdown.getCount() - 1);
            if (Arrays.asList(countdown.getRun()).contains(countdown.getCount()))
                countdown.run();
            countdown.onCount();
            if (countdown.getCount() == 0) {
                countdown.finished();
                AbstractVerany.COUNTDOWNS.remove(countdown);
            }
        }
    }
}
