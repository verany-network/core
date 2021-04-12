package net.verany.api.loader.config;


import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.json.AbstractJsonConfig;
import net.verany.api.loader.LoadObject;
import net.verany.api.loader.Loader;

@Getter
public abstract class ConfigLoader extends Loader {

    private AbstractJsonConfig config;

    public ConfigLoader(AbstractJsonConfig config) {
        if (config != null)
            this.config = config;
        Verany.LOADERS.add(this);
    }

    @Override
    public <T extends LoadObject> void load(LoadInfo<T> loadInfo) {
        T info = config.get(loadInfo.getName(), loadInfo.getType());
        if (info == null)
            info = save(loadInfo);
        loadInfo.setObject(info);
        addInfo(loadInfo);
        onLoadComplete();
    }

    @Override
    public <T extends LoadObject> T save(LoadInfo<T> loadInfo) {
        T t = loadInfo.getObject();
        config.set(loadInfo.getName(), t);
        config.saveConfig();
        return t;
    }

}
