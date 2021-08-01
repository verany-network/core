package net.verany.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;

@Getter
@AllArgsConstructor
@Deprecated
public abstract class AbstractListener {

    private final VeranyPlugin project;

}
