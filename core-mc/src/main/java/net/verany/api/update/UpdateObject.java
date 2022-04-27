package net.verany.api.update;

import com.google.common.collect.Lists;
import net.verany.api.Verany;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.update.date.UpdateEntry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UpdateObject extends DatabaseLoader implements IUpdateObject {

    public UpdateObject(VeranyProject project) {
        super(project, "updates", project.getModule().name().toLowerCase());
        load(new LoadInfo<>("updates", UpdateDataLoader.class, new UpdateDataLoader()));
    }

    @Override
    public void createUpdate(UpdateEntry entry) {
        getUpdates().add(entry);
        save("updates");
    }

    @Override
    public UpdateEntry getUpdate(String id) {
        if (getDataOptional(UpdateDataLoader.class).isEmpty()) return null;
        return getDataOptional(UpdateDataLoader.class).get().getUpdates().stream().filter(entry -> entry.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<UpdateEntry> getUnreadUpdates(UUID uuid) {
        if (getDataOptional(UpdateDataLoader.class).isEmpty()) return new ArrayList<>();
        List<UpdateEntry> readUpdates = getDataOptional(UpdateDataLoader.class).get().getReadUpdates(uuid);
        return Lists.reverse(getUpdates(Comparator.comparing(UpdateEntry::getPublishDate)).stream().filter(entry -> !readUpdates.contains(entry) && entry.isPublished()).collect(Collectors.toList()));
    }

    @Override
    public List<UpdateEntry> getUpdates() {
        if (getDataOptional(UpdateDataLoader.class).isEmpty()) return new ArrayList<>();
        return getDataOptional(UpdateDataLoader.class).get().getUpdates();
    }

    @Override
    public List<UpdateEntry> getUpdates(Comparator<UpdateEntry> c) {
        List<UpdateEntry> updates = new ArrayList<>(getUpdates());
        updates.sort(c);
        return updates;
    }

    @Override
    public void setRead(UUID uuid, UpdateEntry entry) {
        getDataOptional(UpdateDataLoader.class).ifPresent(updateDataLoader -> updateDataLoader.setRead(uuid, entry));
        save("updates");
    }

    @Override
    public boolean hasUnreadUpdate(UUID uuid) {
        return !getUnreadUpdates(uuid).isEmpty();
    }

    public ItemStack createBook(UUID uuid) {
        List<UpdateEntry> updates = getUnreadUpdates(uuid);
        if (updates.isEmpty()) return null;
        IPlayerInfo playerInfo = Verany.getPlayer(uuid);
        List<String> pages = new ArrayList<>();
        for (UpdateEntry update : updates) {
            Deque<String> updateList = new LinkedList<>();
            for (String page : update.getPages())
                updateList.add(ChatColor.translateAlternateColorCodes('&', page));
            updateList.addFirst(playerInfo.getKey("update.published", new Placeholder("%publish%", Verany.getPrettyTime(playerInfo.getCurrentLanguage().getLocale(), update.getPublishDate()))) + "\n\n\n" + ChatColor.translateAlternateColorCodes('&', update.getTopic()));
            pages.addAll(updateList);
        }
        return new ItemBuilder(Material.WRITTEN_BOOK).setBookInfo(new ItemBuilder.BookInfo(updates.get(0).getTitle(), updates.get(0).getAuthor(), pages)).setDisplayName(playerInfo.getKey("update.book.name")).build();
    }
}