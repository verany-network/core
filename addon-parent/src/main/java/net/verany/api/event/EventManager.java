package net.verany.api.event;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager implements IEventManager {

    private final List<VeranyListener> listeners = new ArrayList<>();

    @Override
    public <T extends VeranyListener> void registerListener(T listener) {
        listeners.add(listener);
    }

    @SneakyThrows
    @Override
    public <T extends VeranyEvent> void execute(T event) {
        for (VeranyListener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(VeranyEventHandler.class)) {
                    if(method.getParameterTypes()[0].equals(event.getClass())) {
                        VeranyEvent e = event.getClass().newInstance();
                        method.invoke(e);
                    }
                }
            }
        }
    }
}
