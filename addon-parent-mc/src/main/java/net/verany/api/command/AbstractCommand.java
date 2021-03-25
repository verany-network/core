package net.verany.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.module.VeranyProject;

@AllArgsConstructor
@Getter
public abstract class AbstractCommand {
    private final VeranyProject project;
}
