package net.verany.api.multiproxy;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;

public interface IMultiProxyObject {

    void sendMessage(String channel, String message, JsonDocument document);

    void onMessageIn(String channel, String message, JsonDocument document);

}
