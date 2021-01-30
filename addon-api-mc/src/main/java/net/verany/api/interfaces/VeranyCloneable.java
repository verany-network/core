package net.verany.api.interfaces;

/**
 * @param <T> type of class to clone
 */
public interface VeranyCloneable<T> extends Cloneable {

    /**
     * @return returns method to clone with {@code <T>}
     */
    T clone();

}
