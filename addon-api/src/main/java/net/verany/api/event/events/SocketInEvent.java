package net.verany.api.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.event.VeranyEvent;

@AllArgsConstructor
@Getter
public class SocketInEvent extends VeranyEvent {

    private final String message;

}
