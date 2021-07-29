package net.verany.api.oldmechanics.module;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.oldmechanics.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
@Setter
public class AttackCooldownModule extends AbstractModule {

    private double cooldown = 16;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerJoinEvent event) {
        adjustAttackSpeed(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        adjustAttackSpeed(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        resetAttackSpeed(player);
    }

    private void adjustAttackSpeed(Player player) {
        if (!isEnabled())
            resetAttackSpeed(player);
        else
            setAttackSpeed(player, cooldown);
    }

    @Override
    public void updateState(boolean newState) {
        Bukkit.getOnlinePlayers().forEach(this::adjustAttackSpeed);
    }

    private void setAttackSpeed(Player player, double attackSpeed) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        double baseValue = attribute.getBaseValue();

        if (baseValue != attackSpeed) {
            attribute.setBaseValue(attackSpeed);
            player.saveData();
        }
    }

    private void resetAttackSpeed(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attribute.getBaseValue() != attribute.getDefaultValue()) {
            attribute.setBaseValue(attribute.getDefaultValue());
            player.saveData();
        }
    }
}
