package net.verany.api.websocket;

import net.verany.api.AbstractVerany;
import net.verany.api.module.VeranyProject;

public record CheckTask(VeranyProject project) implements Runnable {

    @Override
    public void run() {
        if (AbstractVerany.MESSENGER != null && AbstractVerany.MESSENGER.closed) {
            boolean success = true;
            try {
                AbstractVerany.MESSENGER.reconnect();
            } catch (IllegalStateException e) {
                System.out.println("Could not connect, try again in 5 seconds..");
                success = false;
            } finally {
                if (success)
                    AbstractVerany.MESSENGER.auth(project);
            }
        }
    }
}
