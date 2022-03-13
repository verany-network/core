package net.verany.api;

import com.mongodb.client.model.Filters;
import net.verany.api.loader.Loader;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
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
            //connectToWebsocket();
            new Thread(mainTask, "core-main-thread").start();
        }

        // TODO: ONLY FOT TESTING PURPOSES
        onSocketConnect.run();

    }

    public static void shutdown(VeranyProject project) {
        project.getConnection().getCollection("socket", "sockets").deleteOne(Filters.eq("key", KEY));
        LOADERS.forEach(Loader::save);
        project.getConnection().disconnect();
    }

}
