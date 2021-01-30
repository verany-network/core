package net.verany.api.player.friend.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class FriendData {
    private final UUID uuid;
    private final long timestamp = System.currentTimeMillis();
}
