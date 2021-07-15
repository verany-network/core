package net.verany.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommandEntry {

    private final String name;
    private String permission;
    private TabCompleter tabCompleter;
    private final List<String> aliases = new ArrayList<>();

    public void addAlias(String... alias) {
        this.aliases.addAll(Arrays.asList(alias));
    }

}
