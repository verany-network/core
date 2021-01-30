package net.verany.api.loader.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.loader.LoadObject;

@AllArgsConstructor
@Getter
public abstract class DatabaseLoadObject implements LoadObject {
    private final String uuid;
}
