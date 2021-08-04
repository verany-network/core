package net.verany.api.module;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class VeranyPlugin implements Serializable, VeranyProject {

    private VeranyModule module;
    private VeranyModule.DatabaseConnection connection;

    public void init() {
    }

    public abstract void enable();

    public abstract void disable();

}
