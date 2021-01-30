package net.verany.api;

import net.verany.api.event.EventManager;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyModule.DatabaseConnection;
import net.verany.api.module.VeranyProject;

public class Verany extends AbstractVerany {

    public static void loadModule(VeranyProject project) {
        VeranyModule module = project.getClass().getAnnotation(VeranyModule.class);

        DatabaseConnection connection = new DatabaseConnection(module);
        project.setModule(module);
        project.setConnection(connection);

        load();
        eventManager = new EventManager();

    }

}
