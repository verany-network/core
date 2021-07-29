package net.verany.api.player.ban;

import net.verany.api.interfaces.IDefault;

import java.util.UUID;

public interface IBanEntry extends IDefault<UUID> {

    UUID getSource();

    long getCreated();

    long getExpire();

    void setExpire(long time);

    String getReason();

    void setReason(String reason);

    void ban();

}
