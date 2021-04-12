package net.verany.api.messaging;

import net.verany.api.Verany;
import net.verany.api.event.events.MessageInEvent;
import net.verany.api.module.VeranyProject;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class VeranyMessenger extends WebSocketClient {

    private final Map<String, Consumer<JSONObject>> callbackMap = new HashMap<>();
    private VeranyProject project;

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

        Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> Bukkit.getPluginManager().callEvent(new MessageInEvent(jsonObject)));

        if (jsonObject.has("id")) {
            String key = jsonObject.getString("id");
            if (!callbackMap.containsKey(key)) return;
            callbackMap.get(key).accept(jsonObject);
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
        object.put("cmd", "redirect");
        object.put("type", type);
        object.put("id", Verany.KEY);
        callbackMap.put(Verany.KEY, callback);
        sendMessage(object);
    }

    public void auth(VeranyProject project,  @NotNull String key) {
        this.project = project;
        sendMessage(new JSONObject().put("cmd", "auth").put("key", key));
    }
}
