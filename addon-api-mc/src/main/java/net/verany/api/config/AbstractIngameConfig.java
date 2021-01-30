package net.verany.api.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractIngameConfig<T> {

    private final Class<T> tClass;
    private final T defaultValue;
    private T value;

    public AbstractIngameConfig(Class<T> tClass, T defaultValue) {
        this.tClass = tClass;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }
    public void setValue(T value) {
        this.value = value;
    }
}
