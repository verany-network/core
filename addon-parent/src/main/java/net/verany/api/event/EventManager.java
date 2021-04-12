package net.verany.api.event;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class EventManager implements IEventManager {

    private final List<VeranyListener> listeners = new ArrayList<>();

    @Override
    public <T extends VeranyListener> void registerListener(T listener) {
        listeners.add(listener);
    }

}
