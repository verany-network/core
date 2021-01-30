package net.verany.api.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.verany.api.itembuilder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Builder
@Getter
@Setter
public class VeranyItem {

    private final Material material;
    private final String name;
    private int amount;
    private final Map<String, Integer> enchantmentMap;
    private final List<PotionEffect> potionEffects;
    private final List<String> lore;

    public static VeranyItem fromItemStack(ItemStack itemStack) {
        Map<String, Integer> enchantmentMap = new HashMap<>();
        itemStack.getEnchantments().forEach((enchantment, integer) -> enchantmentMap.put(enchantment.getName(), integer));

        List<PotionEffect> potionEffects = new ArrayList<>();
        if (itemStack.getType().equals(Material.POTION)) {
            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            potionEffects.addAll(meta.getCustomEffects());
        }

        if(itemStack.getType().equals(Material.PLAYER_HEAD)) {
            if(itemStack.getItemMeta() instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                System.out.println(skullMeta.getPlayerProfile().getProperties().iterator().next().getSignature());
            }
        }

        return VeranyItem.builder().material(itemStack.getType()).name(itemStack.getItemMeta().getDisplayName().equals("") ? itemStack.getType().name() : itemStack.getItemMeta().getDisplayName()).amount(itemStack.getAmount()).enchantmentMap(enchantmentMap).potionEffects(potionEffects).lore(itemStack.getLore()).build();
    }

    public ItemStack getItemStack() {
        ItemBuilder itemBuilder = new ItemBuilder(material);
        itemBuilder.setDisplayName(name);
        itemBuilder.setAmount(amount);
        if (lore != null)
            itemBuilder.addLoreAll(lore);
        enchantmentMap.forEach((s, integer) -> itemBuilder.addEnchantment(Enchantment.getByName(s), integer));
        potionEffects.forEach(itemBuilder::addPotionEffect);
        return itemBuilder.build();
    }

}
