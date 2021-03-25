package net.verany.api.gamemode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractGameMode {

    private final String name;
    private final String[] targetGroup;
    private final String[] databaseNames;

}
