package net.verany.api.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class AbstractSkinData {
    private final String signature;
    private final String value;
    private UUID uuid;
    private String name;
}
