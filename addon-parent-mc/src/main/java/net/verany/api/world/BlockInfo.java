package net.verany.api.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.block.data.BlockData;

@AllArgsConstructor
@Getter
public class BlockInfo {

    private final String info;
    private final BlockData blockData;

}
