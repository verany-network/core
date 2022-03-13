package net.verany.api.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

public class DatabaseManager {

    private final String user;
    private final String server;
    private final String password;
    @Getter
    private final String databaseName;
    private MongoClient client;
    private MongoDatabase mongoDatabase;

    public DatabaseManager(String user, String server, String password, String databaseName) {
        this.user = user;
        this.server = server;
        this.password = password;
        this.databaseName = databaseName;
        this.connect();
    }

    public void connect() {
        String uriString = server.equals("127.0.0.1") ||server.equals("localhost") ? "mongodb://localhost:27017/?authSource=admin" : "mongodb://" + this.user + ":" + this.password + "@" + server + "/?authSource=admin";
        MongoClientURI uri = new MongoClientURI(uriString);
        client = new MongoClient(uri);
        this.mongoDatabase = this.client.getDatabase(this.databaseName);
    }

    public void disconnect() {
        client.close();
    }

    public MongoCollection<Document> getCollection(String name) {
        return this.mongoDatabase.getCollection(name);
    }
}
