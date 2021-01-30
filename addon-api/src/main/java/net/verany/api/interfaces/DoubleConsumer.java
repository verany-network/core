package net.verany.api.interfaces;

/**
 * @param <T> first type
 * @param <U> second type
 */
public interface DoubleConsumer<T, U> {

    /**
     * @param t {@code <T>}
     * @param u {@code <T>}
     */
    void accept(T t, U u);

}
