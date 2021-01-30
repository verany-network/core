package net.verany.api.rarity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum RarityType {

    LEGENDARY(ChatColor.DARK_PURPLE),
    EXCLUSIVE(ChatColor.AQUA),
    RARE(ChatColor.RED),
    NORMAL(ChatColor.DARK_GREEN),
    COMMON(ChatColor.GREEN);

    private final ChatColor chatColor;

}
