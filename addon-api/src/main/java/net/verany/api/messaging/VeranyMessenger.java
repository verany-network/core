package net.verany.api.messaging;

import net.verany.api.AbstractVerany;
import net.verany.api.event.events.MessageInEvent;
import net.verany.api.module.VeranyProject;
import org.bson.Document;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class VeranyMessenger extends WebSocketClient {

    private final Map<String, Consumer<JSONObject>> callbackMap = new HashMap<>();

    public VeranyMessenger(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("opened connection");
    }

    @Override
    public void onMessage(String message) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(message);
        } catch (Exception e) {
            return;
        }
        MessageInEvent messageEvent = new MessageInEvent(jsonObject);
        AbstractVerany.eventManager.getListeners().forEach(veranyListener -> veranyListener.onMessageIn(messageEvent));

        if (jsonObject.has("id")) {
            String key = jsonObject.getString("id");
            if (!callbackMap.containsKey(key)) return;
            callbackMap.get(key).accept(jsonObject);
            callbackMap.remove(key);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void sendMessage(JSONObject object) {
        send(object.toString());
    }

    public void sendMessage(@Nullable String type, @NotNull JSONObject object, @NotNull Consumer<JSONObject> callback) {
        String key = AbstractVerany.generate(10);
        object.put("cmd", "redirect");
        object.put("type", type);
        object.put("id", key);
        callbackMap.put(key, callback);
        sendMessage(object);
    }

    public String auth(VeranyProject project) {
        String key = AbstractVerany.generate(10);
        project.getConnection().getCollection("socket", "sockets").insertOne(new Document().append("key", key).append("type", project.getModule().name()));
        sendMessage(new JSONObject().put("cmd", "auth").put("key", key));
        return key;
    }
}
