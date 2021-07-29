package net.verany.api.loader.proxy;

import com.google.gson.Gson;
import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.loader.LoadObject;
import net.verany.api.loader.Loader;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.module.VeranyProject;
import org.json.JSONObject;

@Getter
public abstract class ProxyLoader extends Loader {

    private final VeranyProject project;
    private final Gson gson = AbstractVerany.GSON;

    public ProxyLoader(VeranyProject project) {
        this.project = project;
    }

    @Override
    public <T extends LoadObject> void load(LoadInfo<T> loadInfo) {
        if (getInfo(loadInfo.getType()) != null)
            getInfoLists().remove(getInfo(loadInfo.getType()));
        String key = toDatabaseInfo(loadInfo).getUuid();
        T info = AbstractVerany.MESSENGER.get(key, loadInfo.getType());
        if (info == null)
            info = AbstractVerany.MESSENGER.update(key, loadInfo.getType(), loadInfo.getDefaultObject());
        loadInfo.setObject(info);
        addInfo(loadInfo);
        onLoadComplete();
    }

    @Override
    public <T extends LoadObject> T save(LoadInfo<T> loadInfo) {
        T t = loadInfo.getObject();
        String key = toDatabaseInfo(loadInfo).getUuid();
        AbstractVerany.MESSENGER.update(key, loadInfo.getType(), t);
        return t;
    }

    private <T extends LoadObject> DatabaseLoadObject toDatabaseInfo(LoadInfo<T> loadInfo) {
        return (DatabaseLoadObject) loadInfo.getObject();
    }
}
