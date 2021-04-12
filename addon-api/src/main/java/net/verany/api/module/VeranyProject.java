package net.verany.api.module;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.module.VeranyModule.DatabaseConnection;

import java.io.Serializable;

@Getter
@Setter
public abstract class VeranyProject implements Serializable {

    private VeranyModule module;
    private DatabaseConnection connection;

    public abstract void enable();

    public abstract void disable();

}
