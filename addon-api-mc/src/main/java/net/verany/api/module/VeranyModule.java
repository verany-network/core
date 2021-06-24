package net.verany.api.module;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.verany.api.database.DatabaseManager;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.VeranyGameMode;
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
        private final VeranyModule module;
        private final List<DatabaseManager> databaseManagers;

        public DatabaseConnection(VeranyModule module, String user, String hostname, String password, String[] databases) {
            this.module = module;
            this.databaseManagers = new ArrayList<>();

            for (String database : databases)
                databaseManagers.add(new DatabaseManager(user, hostname, password, database));
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
    }

}
