package net.verany.api.oldmechanics.module;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.oldmechanics.AbstractModule;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Setter
@Getter
public class ProjectileKnockbackModule extends AbstractModule {

    private double snowballDamage;
    private double eggDamage;
    private double enderpearlDamage;

    public ProjectileKnockbackModule() {
        snowballDamage = 0.01;
        eggDamage = 0.01;
        enderpearlDamage = 0.01;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityHit(EntityDamageByEntityEvent e) {
        EntityType type = e.getDamager().getType();

        switch (type) {
            case SNOWBALL:
                e.setDamage(snowballDamage);
                break;
            case EGG:
                e.setDamage(eggDamage);
                break;
            case ENDER_PEARL:
                e.setDamage(enderpearlDamage);
                break;
            default:
                break;
        }

    }
}
