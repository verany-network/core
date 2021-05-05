package net.verany.api.player.afk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.verany.api.config.IngameConfig;
import net.verany.api.event.events.PlayerAfkEvent;
import net.verany.api.module.VeranyProject;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
@Setter
public class AfkObject implements IAFKObject {

    private final VeranyProject project;
    private final String[] names = {"", "§9Z", "§9ZZ", "§9ZZz", "§9ZZzz"};

    private UUID key;

    private boolean afk = false;
    private long afkTime = System.currentTimeMillis();
    private final List<CheckType> enabledChecks = new ArrayList<>(Arrays.asList(CheckType.values()));

    private ArmorStand armorStand;
    private int count = 0;

    @Override
    public void load(UUID key) {
        this.key = key;
        resetAfkTime();
    }

    @Override
    public void update() {
        if (isAfk()) {
            removeHologram();
            setAfk(false);
        }
        resetAfkTime();
    }

    @Override
    public boolean shouldAfk() {
        return System.currentTimeMillis() > afkTime + IngameConfig.AFK_TIME.getValue();
    }

    @Override
    public void resetAfkTime() {
        afkTime = System.currentTimeMillis();
        if (afk) {
            setAfk(false);
            removeHologram();
        }
    }

    @Override
    public void spawnHologram() {
        armorStand = player().getWorld().spawn(player().getLocation().add(0, 0.2, 0), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName("§9" + names[0]);

        player().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 2, true, false, false));
    }

    @Override
    public void removeHologram() {
        if (player() != null)
            player().removePotionEffect(PotionEffectType.BLINDNESS);

        armorStand.remove();
    }

    @Override
    public void updateHologram() {
        if (armorStand == null) return;
        count++;
        if (count == names.length)
            count = 0;
        armorStand.setCustomName("§9" + names[count]);
    }

    @Override
    public boolean isCheckEnabled(CheckType checkType) {
        return enabledChecks.contains(checkType);
    }

    @Override
    public void enableAfkCheck(CheckType... checkType) {
        enabledChecks.addAll(Arrays.asList(checkType));
    }

    @Override
    public void disableAfkCheck(CheckType... checkTypes) {
        enabledChecks.removeIf(checkType -> Arrays.asList(checkTypes).contains(checkType));
    }

    public void setAfk(boolean afk) {
        this.afk = afk;
        if (player() != null)
            Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> Bukkit.getPluginManager().callEvent(new PlayerAfkEvent(player())));
    }
}
