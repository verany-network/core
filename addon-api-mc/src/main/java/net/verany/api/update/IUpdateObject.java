package net.verany.api.update;

import lombok.Getter;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.update.date.UpdateEntry;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public interface IUpdateObject {

    void createUpdate(UpdateEntry entry);

    UpdateEntry getUpdate(String id);

    List<UpdateEntry> getUnreadUpdates(UUID uuid);

    List<UpdateEntry> getUpdates();

    List<UpdateEntry> getUpdates(Comparator<UpdateEntry> c);

    void setRead(UUID uuid, UpdateEntry entry);

    boolean hasUnreadUpdate(UUID uuid);

    ItemStack createBook(UUID uuid);

    @Getter
    class UpdateDataLoader extends DatabaseLoadObject {

        private final List<UpdateEntry> updates = new ArrayList<>();
        private final Map<UUID, List<String>> readUpdates = new HashMap<>();

        public UpdateDataLoader() {
            super("updates");
        }

        public List<UpdateEntry> getReadUpdates(UUID uuid) {
            readUpdates.putIfAbsent(uuid, new ArrayList<>());
            return readUpdates.get(uuid).stream().map(s -> updates.stream().filter(entry -> entry.getId().equals(s)).findFirst().orElse(null)).collect(Collectors.toList());
        }

        public void setRead(UUID uuid, UpdateEntry entry) {
            readUpdates.get(uuid).add(entry.getId());
        }
    }

}
