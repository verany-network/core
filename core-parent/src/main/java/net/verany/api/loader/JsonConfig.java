package net.verany.api.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.verany.api.AbstractVerany;
import net.verany.api.loader.config.AbstractJsonConfig;

import java.io.*;

public class JsonConfig extends AbstractJsonConfig {

    public JsonConfig(File file) {
        super(file);
        reloadFromFile();
    }

    @Override
    public void saveConfig() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (final IOException e) {
                return;
            }
        }
        try (final FileWriter writer = new FileWriter(this.file)) {
            writer.write(AbstractVerany.GSON.toJson(this.object));
        } finally {
            return;
        }
    }

    @Override
    public void reloadFromFile() {
        if (!this.file.exists()) {
            return;
        }
        try {
            this.object = AbstractVerany.GSON.fromJson(new FileReader(file), JsonObject.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /*try (final FileReader reader = new FileReader(this.file)) {
            this.object = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (final IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public <T> T get(String key, Class<T> tClass) {
        if (!this.object.has(key)) return null;
        return AbstractVerany.GSON.fromJson(this.object.get(key), tClass);
    }

    @Override
    public <T> void set(String key, T t) {
        this.object.add(key, AbstractVerany.GSON.toJsonTree(t));
    }

    @Override
    public void fromString(String string) {
        this.object = new JsonParser().parse(string).getAsJsonObject();
    }
}