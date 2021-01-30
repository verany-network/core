package net.verany.api.prefix;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

@AllArgsConstructor
@Getter
public class PrefixColor {
    private final ChatColor firstColor;
    private final ChatColor secondColor;
}
