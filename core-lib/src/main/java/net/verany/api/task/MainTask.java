package net.verany.api.task;

import lombok.Setter;
import net.verany.api.AbstractVerany;

@Setter
@Deprecated
public class MainTask implements Runnable {

    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            for (AbstractTask task : AbstractVerany.TASKS) {
                if (System.currentTimeMillis() > task.getLastUpdate()) {
                    task.setLastUpdate(task.getWaitTime() + System.currentTimeMillis());
                    task.run();
                }
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
