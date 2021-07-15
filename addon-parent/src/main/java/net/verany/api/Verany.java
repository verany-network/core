package net.verany.api;

import lombok.SneakyThrows;
import net.verany.api.event.EventManager;
import net.verany.api.websocket.CheckTask;
import net.verany.api.websocket.VeranyMessenger;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyModule.DatabaseConnection;
import net.verany.api.module.VeranyProject;

import java.net.URI;

public class Verany extends AbstractVerany {

    @SneakyThrows
    public static void loadModule(VeranyProject project) {
        VeranyModule module = project.getClass().getAnnotation(VeranyModule.class);

        DatabaseConnection connection = new DatabaseConnection(module);
        project.setModule(module);
        project.setConnection(connection);

        if(!loaded) {
            messenger = new VeranyMessenger(new URI("wss://wss.verany.net:888"));
            messenger.connect();
            Verany.addTask(new CheckTask(5000L, project));
        }

        load();
        eventManager = new EventManager();

    }

}
