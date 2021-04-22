package net.verany.api.messaging;

import net.verany.api.AbstractVerany;
import net.verany.api.module.VeranyProject;
import net.verany.api.task.AbstractTask;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

public class CheckTask extends AbstractTask {

    private final VeranyProject project;

    public CheckTask(long waitTime, VeranyProject project) {
        super(waitTime);
        this.project = project;
    }

    @Override
    public void run() {
        if (AbstractVerany.messenger != null && AbstractVerany.messenger.closed) {
            boolean success = true;
            try {
                AbstractVerany.messenger.connect();
            } catch (IllegalStateException e) {
                System.out.println("Could not connect, try again in 5 seconds..");
                success = false;
            } finally {
                if (success)
                    AbstractVerany.messenger.auth(project);
            }
        }
    }
}
