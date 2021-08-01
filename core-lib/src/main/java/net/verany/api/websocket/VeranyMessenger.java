package net.verany.api.websocket;

import com.mongodb.client.model.Filters;
import net.verany.api.AbstractVerany;
import net.verany.api.loader.LoadObject;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class VeranyMessenger extends WebSocketClient {

    private final Map<String, Consumer<JSONObject>> callbackMap = new HashMap<>();
    private Runnable onClose;
    public boolean closed = false;

    public VeranyMessenger(URI serverUri) {
        super(serverUri);
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

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
        AbstractVerany.SOCKET_OPEN.forEach(Runnable::run);
    }

    @Override
    public void onMessage(String message) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(message);
        } catch (Exception e) {
            return;
        }

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
        onClose.run();

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
        sendMessage("redirect", type, object, callback);
    }

    public void sendMessage(@NotNull String cmd, @Nullable String type, @NotNull JSONObject object, @NotNull Consumer<JSONObject> callback) {
        object.put("cmd", cmd);
        object.put("type", type);
        object.put("id", AbstractVerany.KEY);
        callbackMap.put(AbstractVerany.KEY, callback);
        sendMessage(object);
    }

    public <T> T update(String key, Class<T> tClass, T object) {
        AtomicReference<T> atomicReference = new AtomicReference<>(null);
        sendMessage("update", "db", new JSONObject().put("class", tClass).put("object", object).put("key", key), jsonObject -> atomicReference.set((T) jsonObject.get("result")));
        return atomicReference.get();
    }

    public <T> T get(String key, Class<T> tClass) {
        AtomicReference<T> atomicReference = new AtomicReference<>(null);
        sendMessage("get", "db", new JSONObject().put("class", tClass).put("key", key), jsonObject -> atomicReference.set((T) jsonObject.get("result")));
        return atomicReference.get();
    }

    public void auth(VeranyProject project) {
        String key = AbstractVerany.KEY;
        if (closed) return;
        onClose = () -> project.getConnection().getCollection("socket", "socket").deleteOne(Filters.eq("key", key));
        project.getConnection().getCollection("socket", "sockets").insertOne(new Document().append("key", key).append("type", project.getModule().name()));
        sendMessage(new JSONObject().put("cmd", "auth").put("key", key));
    }
}