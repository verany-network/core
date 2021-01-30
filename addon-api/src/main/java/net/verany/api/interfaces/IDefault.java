package net.verany.api.interfaces;

import net.verany.api.module.VeranyProject;

public interface IDefault<T> {

    void load(T key);

    void update();

    T getUniqueId();

}
