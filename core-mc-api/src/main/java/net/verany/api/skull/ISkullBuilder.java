package net.verany.api.skull;

import net.verany.api.interfaces.VeranyCloneable;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ISkullBuilder extends VeranyCloneable<ISkullBuilder> {

    /**
     * @param name name of item
     * @return returns {@link ISkullBuilder ISkullBuilder.class}
     */
    ISkullBuilder setDisplayName(String name);

    /**
     * @param amount amount of item
     * @return returns {@link ISkullBuilder ISkullBuilder.class}
     */
    ISkullBuilder setAmount(int amount);

    /**
     * @param line lore line of item
     * @return returns {@link ISkullBuilder ISkullBuilder.class}
     */
    ISkullBuilder addLoreLine(String line);

    /**
     * @param lines string array of lore lines of item
     * @return returns {@link ISkullBuilder ISkullBuilder.class}
     */
    ISkullBuilder addLoreArray(String... lines);

    /**
     * @param lines list of lore lines of item
     * @return returns {@link ISkullBuilder ISkullBuilder.class}
     */
    ISkullBuilder addLoreAll(List<String> lines);

    /**
     * @return returns final item as {@link ItemStack ItemStack.class}
     */
    ItemStack build();

    /**
     * @param block block in {@link org.bukkit.World World.class}
     * @return returns if place success
     */
    boolean setBlock(Block block);

}
