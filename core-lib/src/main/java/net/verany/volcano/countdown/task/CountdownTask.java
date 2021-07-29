package net.verany.volcano.countdown.task;

import net.verany.api.AbstractVerany;
import net.verany.volcano.countdown.AbstractCountdown;

import java.util.Arrays;

public class CountdownTask implements Runnable {

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
                //AbstractVerany.COUNTDOWNS.remove(countdown);
            }
        }
    }
}
