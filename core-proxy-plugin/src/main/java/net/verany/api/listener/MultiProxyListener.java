package net.verany.api.listener;

import com.velocitypowered.api.event.Subscribe;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.ext.bridge.velocity.event.VelocityChannelMessageReceiveEvent;
import net.verany.api.Core;
import net.verany.api.Verany;
import net.verany.api.placeholder.Placeholder;

public class MultiProxyListener {

    @Subscribe
    public void onMessage(VelocityChannelMessageReceiveEvent event) {
        Core.INSTANCE.getMultiProxyObject().onMessageIn(event.getChannel(), event.getMessage(), event.getData());
    }

}
