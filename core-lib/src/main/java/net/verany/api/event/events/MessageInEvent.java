package net.verany.api.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.event.VeranyEvent;
import org.json.JSONObject;

@AllArgsConstructor
@Getter
public class MessageInEvent extends VeranyEvent {

    private final JSONObject message;

    public MessageInEvent() {
        message = null;
    }

    public void answer(JSONObject object) {
        AbstractVerany.MESSENGER.sendMessage(object.put("cmd", "answer"));
    }

}
