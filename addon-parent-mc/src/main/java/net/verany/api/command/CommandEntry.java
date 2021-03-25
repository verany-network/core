package net.verany.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.TabCompleter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommandEntry {

    private final String name;
    private String permission;
    private TabCompleter tabCompleter;

}
