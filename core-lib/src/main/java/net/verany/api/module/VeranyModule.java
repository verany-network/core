package net.verany.api.module;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.api.database.Database;
import net.verany.api.database.DatabaseManager;
import org.bson.Document;

import java.io.*;
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

    String prefix() default "";

    String version();

    String[] authors();

    String parentConnectionPath() default "plugins/%name%";

    Service[] disabledServices() default {};

    enum Service {
        PLAYER,
        MESSAGES,
        LANGUAGES,
        WEBSOCKET,
        DATABASE,
        PERMISSION_GROUPS
    }

    @Getter
    class DatabaseConnection {

        private final VeranyModule module;
        private final File file;
        private final List<DatabaseManager> databaseManagers;

        public DatabaseConnection(VeranyModule module) {
            this.module = module;
            this.file = new File(module.parentConnectionPath().replace("%name%", module.name()), "database.json");
            this.databaseManagers = new ArrayList<>();

            initJsonConfig();
            loadConnection();
        }

        private void loadConnection() {
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
            return databaseManagers.stream().filter(databaseManager -> databaseManager.getDatabaseName().equalsIgnoreCase(name)).findFirst().orElse(null);
        }

        public void disconnect() {
            databaseManagers.forEach(DatabaseManager::disconnect);
        }

        @SneakyThrows
        private void initJsonConfig() {
            if (file.exists()) return;
            Gson gson = new Gson();

            Database database = new Database("user", "password", "127.0.0.1", new String[]{"database1", "database2"});

            new File(module.parentConnectionPath().replace("%name%", module.name())).mkdirs();
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
