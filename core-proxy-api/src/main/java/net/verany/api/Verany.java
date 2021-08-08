package net.verany.api;

import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.task.MainTask;

public class Verany extends AbstractVerany {


    public static void loadModule(VeranyPlugin project, Runnable onSocketConnect) {
        VeranyModule module = project.getClass().getAnnotation(VeranyModule.class);

        VeranyModule.DatabaseConnection connection = new VeranyModule.DatabaseConnection(module);
        project.setModule(module);
        project.setConnection(connection);

        AbstractVerany.SOCKET_OPEN.add(onSocketConnect);

        if (mainTask == null) {
            mainTask = new MainTask();

        }

    }

}
