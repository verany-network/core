package net.verany.api.module;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.api.database.Database;
import net.verany.api.database.DatabaseManager;
import org.bson.Document;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
public @interface VeranyModule {

    String name();

    String version();

    String[] authors();

    @Getter
    class DatabaseConnection {

        private final File file;

        private final VeranyModule module;
        private final List<DatabaseManager> databaseManagers;

        public DatabaseConnection(VeranyModule module) {
            this.module = module;
            file = new File("module/" + module.name(), "database.json");
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

        @SneakyThrows
        private void initJsonConfig() {
            if (file.exists()) return;
            Gson gson = new Gson();

            Database database = new Database("user", "password", "127.0.0.1", new String[]{"database1", "database2"});

            new File("module/" + module.name()).mkdirs();
            file.createNewFile();

            try (FileWriter fileWriter = new FileWriter(file)) {
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
