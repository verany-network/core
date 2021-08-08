package net.verany.api.multiproxy;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;

public class MultiProxyObject implements IMultiProxyObject{

    @Override
    public void sendMessage(String channel, String message, JsonDocument document) {
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage(channel, message, document);
    }

    @Override
    public void onMessageIn(String channel, String message, JsonDocument document) {

    }
}
