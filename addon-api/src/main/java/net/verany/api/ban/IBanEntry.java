package net.verany.api.ban;

import lombok.NonNull;

import java.util.UUID;

public interface IBanEntry {

    UUID getTarget();

    UUID getSource();

    long getCreated();

    long getExpire();

    void setExpire(long time);

    String getReason();

    void setReason(String reason);

    void save();

}
