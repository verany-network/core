package net.verany.api.oldmechanics.module;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.verany.api.oldmechanics.AbstractModule;
import net.verany.api.oldmechanics.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@Getter
@Setter
public class PlayerRegenModule extends AbstractModule {

    protected final Object2LongMap<UUID> healTimes = new Object2LongOpenHashMap<>();
    private long frequency;
    private int amount;
    private float exhaustion;

    public PlayerRegenModule() {
        frequency = 3 * 1000L;
        amount = 1;
        exhaustion = 3;
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        healTimes.removeLong(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onRegen(EntityRegainHealthEvent e) {
        if (e.getEntityType() != EntityType.PLAYER || e.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED)
            return;

        final Player p = (Player) e.getEntity();

        e.setCancelled(true);

        long lastHealTime = getLastHealTime(p);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastHealTime < frequency) return;

        double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        if (p.getHealth() < maxHealth) {
            p.setHealth(MathHelper.clamp(p.getHealth() + amount, 0.0, maxHealth));
            healTimes.put(p.getUniqueId(), currentTime);
        }

        final float previousExhaustion = p.getExhaustion();
        Bukkit.getScheduler().runTaskLater(getProject(), () -> {
            p.setExhaustion(previousExhaustion + exhaustion);
        }, 1L);
    }

    private long getLastHealTime(Player p) {
        return healTimes.getLong(p.getUniqueId());
    }
}
