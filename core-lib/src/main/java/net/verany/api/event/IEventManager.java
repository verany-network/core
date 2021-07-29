package net.verany.api.event;

import java.util.List;

public interface IEventManager {

    <T extends VeranyListener> void registerListener(T listener);

    List<VeranyListener> getListeners();

    void call(VeranyEvent listener);

}
