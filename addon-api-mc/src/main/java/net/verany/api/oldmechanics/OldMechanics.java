package net.verany.api.oldmechanics;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import net.verany.api.AbstractVerany;
import net.verany.api.module.VeranyProject;
import net.verany.api.oldmechanics.module.AttackCooldownModule;
import net.verany.api.oldmechanics.module.PlayerRegenModule;
import net.verany.api.oldmechanics.module.ProjectileKnockbackModule;
import net.verany.api.oldmechanics.module.SwordBlockingModule;

@AllArgsConstructor
@Getter
public class OldMechanics<T extends AbstractModule> {

    public static final OldMechanics<AttackCooldownModule> ATTACK_COOLDOWN = new OldMechanics<>(new AttackCooldownModule());
    public static final OldMechanics<PlayerRegenModule> PLAYER_REGEN = new OldMechanics<>(new PlayerRegenModule());
    public static final OldMechanics<ProjectileKnockbackModule> PROJECTILE_KNOCKBACK = new OldMechanics<>(new ProjectileKnockbackModule());
    public static final OldMechanics<SwordBlockingModule> SWORD_BLOCKING = new OldMechanics<>(new SwordBlockingModule());

    private final T value;

    public void enable(VeranyProject project) {
        value.setEnabled(true, project);
        AbstractVerany.EVENT_REGISTRY.registerListener(value);
    }

    public void disable(VeranyProject project) {
        value.setEnabled(false, project);
        AbstractVerany.EVENT_REGISTRY.unregisterListener(value);
    }

}
