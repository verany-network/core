package net.verany.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.module.VeranyProject;

@Getter
@AllArgsConstructor
public abstract class AbstractListener {

    private final VeranyProject project;

}
