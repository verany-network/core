package net.verany.api.list;

import java.util.Collection;

public interface IListHandler {

    <T> T getNext(Collection<T> collection, T target);

    <T> T getNext(Collection<T> collection, T target, boolean circular);

    <T> T getPrevious(Collection<T> collection, T target);

    <T> T getPrevious(Collection<T> collection, T target, boolean circular);

}
