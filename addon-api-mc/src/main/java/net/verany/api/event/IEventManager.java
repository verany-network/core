package net.verany.api.event;

public interface IEventManager {

    <T extends VeranyListener> void registerListener(T listener);

    <T extends VeranyEvent> void execute(T event);

}
