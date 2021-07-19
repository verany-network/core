package net.verany.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.command.executor.VeranyCommandExecutor;
import net.verany.api.module.VeranyProject;

@AllArgsConstructor
@Getter
public abstract class AbstractCommand {
    private final VeranyProject project;

    public void registerCommand(CommandEntry commandEntry, VeranyCommandExecutor executor) {
        Verany.registerCommand(project, commandEntry, executor);
    }
}
