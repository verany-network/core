package net.verany.api.loader.database;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.loader.LoadObject;
import net.verany.api.loader.Loader;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import org.bson.Document;

import java.util.UUID;

@Getter
public abstract class DatabaseLoader extends Loader {

    private final VeranyProject project;
    private final String collection;
    private final String database;
    private final Gson gson = Verany.GSON;

    public DatabaseLoader(VeranyProject project, String collection) {
        this(project, collection, project.getConnection().getDatabaseManagers().get(0).getDatabaseName());
    }

    public DatabaseLoader(VeranyProject project, String collection, String database) {
        this.project = project;
        this.collection = collection;
        this.database = database;
        Verany.LOADERS.add(this);
    }

    @Override
    public <T extends LoadObject> void load(LoadInfo<T> loadInfo) {
        if (getInfo(loadInfo.getType()) != null) {
            onLoadComplete();
            return;
        }
        T info;
        String key = databaseInfo(loadInfo).getUuid();
        Document first = project.getConnection().getCollection(database, collection).find(Filters.eq("uuid", key)).first();
        if (first == null)
            info = insert(loadInfo);
        else {
            info = new Gson().fromJson(first.toJson(), loadInfo.getType());
        }
        loadInfo.setObject(info);
        addInfo(loadInfo);
        onLoadComplete();
    }

    @Override
    public <T extends LoadObject> T save(LoadInfo<T> loadInfo) {
        T t = loadInfo.getObject();
        String key = databaseInfo(loadInfo).getUuid();
        project.getConnection().getCollection(database, collection).replaceOne(new BasicDBObject("uuid", key), Verany.GSON.fromJson(Verany.GSON.toJson(t), Document.class));
        return t;
    }

    private <T extends LoadObject> T insert(LoadInfo<T> loadInfo) {
        project.getConnection().getCollection(database, collection).insertOne(Verany.GSON.fromJson(Verany.GSON.toJson(loadInfo.getObject()), Document.class));
        return loadInfo.getObject();
    }

    private <T extends LoadObject> DatabaseLoadObject databaseInfo(LoadInfo<T> loadInfo) {
        return (DatabaseLoadObject) loadInfo.getObject();
    }

}
