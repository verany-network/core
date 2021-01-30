package net.verany.api.itembuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.api.itembuilder.map.ImageRender;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_16_R3.map.CraftMapView;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack item;
    private List<String> lore = new ArrayList<String>();
    private ItemMeta meta;
    private MapInfo mapInfo;

    /**
     * @param item itemstack
     */
    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    /**
     * @param mat    material of item
     * @param amount amount of item
     */
    public ItemBuilder(Material mat, int amount) {
        item = new ItemStack(mat, amount, (short) 0);
        meta = item.getItemMeta();
    }

    /**
     * @param mat material of item
     */
    public ItemBuilder(Material mat) {
        item = new ItemStack(mat, 1, (short) 0);
        meta = item.getItemMeta();
    }

    /**
     * @param value amount of item
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setAmount(int value) {
        item.setAmount(value);
        return this;
    }

    /**
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setNoName() {
        meta.setDisplayName(" ");
        return this;
    }

    /**
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setGlow() {
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * @param value value of item glowing
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setGlow(boolean value) {
        if (value) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.removeEnchant(Enchantment.DURABILITY);
        }
        return this;
    }

    /**
     * @param data durability of item
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setData(short data) {
        item.setDurability(data);
        return this;
    }

    /**
     * @param line lore line of item
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder addLoreLine(String line) {
        lore.add(line);
        return this;
    }

    /**
     * @param lines string array of lore lines of item
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder addLoreArray(String... lines) {
        lore.addAll(Arrays.asList(lines));
        return this;
    }

    /**
     * @param lines list of lore lines of item
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder addLoreAll(List<String> lines) {
        lore.addAll(lines);
        return this;
    }

    /**
     * @param name name of item
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    /**
     * @param c color of item
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setColor(Color c) {
        ((LeatherArmorMeta) meta).setColor(c);
        return this;
    }

    /**
     * add a effect to the item
     *
     * @param effect type of effect
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, true);
    }

    /**
     * add a effect to the item
     *
     * @param effect    type of effect
     * @param overwrite overwrite effects
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder addPotionEffect(PotionEffect effect, boolean overwrite) {
        ((PotionMeta) meta).addCustomEffect(effect, overwrite);
        return this;
    }

    /**
     * @param effect type of effect
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder removePotionEffect(PotionEffectType effect) {
        ((PotionMeta) meta).removeCustomEffect(effect);
        return this;
    }

    public ItemBuilder setBookInfo(BookInfo bookInfo) {
        BookMeta bookMeta = (BookMeta) meta;
        bookMeta.setTitle(bookInfo.getTitle());
        bookMeta.setAuthor(bookInfo.getAuthor());
        bookMeta.setPages(bookInfo.getPages());
        meta = bookMeta;
        return this;
    }

    /**
     * Clear all effects of the item
     *
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder clearEffects() {
        ((PotionMeta) meta).clearCustomEffects();
        return this;
    }

    /**
     * @param value value if unbreakable
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder setUnbreakable(boolean value) {
        meta.setUnbreakable(value);
        return this;
    }

    /**
     * @param ench type of enchantment
     * @param lvl  level of {@code ench}
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder addEnchantment(Enchantment ench, int lvl) {
        meta.addEnchant(ench, lvl, true);
        return this;
    }

    public ItemBuilder setMap(MapInfo map) throws IOException {
        //this.mapInfo = map;
        MapMeta mapMeta = (MapMeta) meta;
        mapMeta.getMapView().getRenderers().clear();
        mapMeta.getMapView().getRenderers().add(new ImageRender(map.getUrl()));
        mapMeta.getMapView().getRenderers().add(new MapRenderer() {
            @Override
            public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                for (MapInfo.MapText mapText : map.getText()) {
                    mapCanvas.drawText(mapText.getX(), mapText.getY(), mapText.getFont(), mapText.getText());
                }
            }
        });
        meta = mapMeta;
        return this;
    }

    /**
     * @param flag array of item flags
     * @return returns {@link ItemBuilder ItemBuilder.class}
     */
    public ItemBuilder addItemFlag(ItemFlag... flag) {
        meta.addItemFlags(flag);
        return this;
    }

    /**
     * @return returns final item as {@link ItemStack ItemStack.class}
     */
    @SneakyThrows
    public ItemStack build() {
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);

        /*if(mapInfo != null) {
            MapView view = Bukkit.createMap(Bukkit.getWorld("world"));
            view.getRenderers().clear();
            view.addRenderer(new ImageRender(mapInfo.getUrl()));
            view.addRenderer(new MapRenderer() {
                @Override
                public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
                    for (MapInfo.MapText mapText : mapInfo.getText()) {
                        mapCanvas.drawText(mapText.getX(), mapText.getY(), mapText.getFont(), mapText.getText());
                    }
                }
            });
            item.setDurability((short) view.getId());
        }*/

        return item;
    }

    @AllArgsConstructor
    @Getter
    public static class BookInfo {
        private final String title, author;
        private final List<String> pages;
    }

    @AllArgsConstructor
    @Getter
    public static class MapInfo {
        private final String url;
        private final List<MapText> text;

        @AllArgsConstructor
        @Getter
        public static class MapText {
            private final int x, y;
            private final MapFont font;
            private final String text;
        }
    }

/*
    @Getter
    public static class TextLore extends ArrayList<String> {
        private final String title;
        private final String text;

        public TextLore(String title, String text) {
            this.title = title;
            this.text = text;

            add("§8» §7" + title);

            String line = "";
            for (int i = 0; i < text.length(); i++) {
                line += text.charAt(i);
                if()
            }
        }
    }*/
}
