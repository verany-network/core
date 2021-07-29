package net.verany.api.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EventManager implements IEventManager {

    private final List<VeranyListener> listeners = new ArrayList<>();

    @Override
    public <T extends VeranyListener> void registerListener(T listener) {
        listeners.add(listener);
    }

    @Override
    public void call(VeranyEvent listener) {
        /*for (VeranyListener veranyListener : listeners) {
            if (listener instanceof MessageInEvent)
                veranyListener.onMessageIn((MessageInEvent) listener);
        }*/
    }
}
