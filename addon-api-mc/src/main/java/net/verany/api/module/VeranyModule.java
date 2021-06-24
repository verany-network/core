package net.verany.api.module;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import lombok.Getter;
import net.verany.api.database.Database;
import net.verany.api.database.DatabaseManager;

import org.bson.Document;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface VeranyModule {

    String name();

    int maxRounds() default -1;

    String prefix();

    String version();

    String[] authors();

    @Getter
    class DatabaseConnection {

        private final File file;

        private final VeranyModule module;
        private final List<DatabaseManager> databaseManagers;

        public DatabaseConnection(VeranyModule module) {
            this.module = module;
            file = new File("plugins//" + module.name() +"//database.json");
            initJsonConfig();
            this.databaseManagers = new ArrayList<>();

            Database databaseClass = loadJsonConfig();

            for (String database : databaseClass.getDatabases())
                databaseManagers.add(new DatabaseManager(databaseClass.getUser(), databaseClass.getHostname(), databaseClass.getPassword(), database));
        }

        public MongoCollection<Document> getCollection(String collection) {
            return databaseManagers.get(0).getCollection(collection);
        }

        public MongoCollection<Document> getCollection(String database, String collection) {
            return getDatabase(database).getCollection(collection);
        }

        public DatabaseManager getDatabase(String name) {
            for (DatabaseManager databaseManager : databaseManagers)
                if (databaseManager.getDatabaseName().equals(name))
                    return databaseManager;
            return null;
        }

        public void disconnect() {
            databaseManagers.forEach(DatabaseManager::disconnect);
        }

        private void initJsonConfig() {
            if (file.exists()) return;
            Gson gson = new Gson();

            Database database = new Database("user", "password", "127.0.0.1", new String[]{"database1", "database2"});
            try (FileWriter fileWriter = new FileWriter(file)) {
                file.createNewFile();
                fileWriter.write(gson.toJson(database));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Database loadJsonConfig() {
            if (!file.exists()) initJsonConfig();

            Gson gson = new Gson();
            try {
                return gson.fromJson(new FileReader(file), Database.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
