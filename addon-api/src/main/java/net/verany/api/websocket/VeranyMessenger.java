package net.verany.api.websocket;

import net.verany.api.AbstractVerany;
import net.verany.api.event.events.MessageInEvent;
import net.verany.api.module.VeranyProject;
import org.bson.Document;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class VeranyMessenger extends WebSocketClient {

    private final Map<String, Consumer<JSONObject>> callbackMap = new HashMap<>();
    public boolean closed = false;
    private final Runnable onOpen;

    public VeranyMessenger(URI serverUri, Runnable onOpen) {
        super(serverUri);
        this.onOpen = onOpen;
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }};

        try {

            SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null, trustAllCerts, new java.security.SecureRandom());

            SSLSocketFactory socketFactory = ssl.getSocketFactory();
            this.setSocketFactory(socketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        closed = false;
        onOpen.run();
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
        closed = true;
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
        object.put("id", AbstractVerany.KEY);
        callbackMap.put(AbstractVerany.KEY, callback);
        sendMessage(object);
    }

    public String auth(VeranyProject project) {
        String key = AbstractVerany.KEY;
        if (closed) return key;
        project.getConnection().getCollection("socket", "sockets").insertOne(new Document().append("key", key).append("type", project.getModule().name()));
        sendMessage(new JSONObject().put("cmd", "auth").put("key", key));
        return key;
    }
}