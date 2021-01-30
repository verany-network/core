package net.verany.api.interfaces;

public interface ILoadObject<T> {

    void load(T key);

    void update();

    T getKey();

}
