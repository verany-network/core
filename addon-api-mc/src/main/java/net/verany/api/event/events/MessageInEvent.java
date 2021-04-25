package net.verany.api.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.event.VeranyEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.json.JSONObject;

@AllArgsConstructor
@Getter
public class MessageInEvent extends Event {

    private final JSONObject message;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public void answer(JSONObject object) {
        AbstractVerany.MESSENGER.sendMessage(object.put("cmd", "answer"));
    }

}
