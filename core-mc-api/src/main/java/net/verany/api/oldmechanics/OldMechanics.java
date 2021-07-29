package net.verany.api.oldmechanics;

import net.verany.api.Verany;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.oldmechanics.module.AttackCooldownModule;
import net.verany.api.oldmechanics.module.PlayerRegenModule;
import net.verany.api.oldmechanics.module.ProjectileKnockbackModule;
import net.verany.api.oldmechanics.module.SwordBlockingModule;

public record OldMechanics<T extends AbstractModule>(T value) {

    public static final OldMechanics<AttackCooldownModule> ATTACK_COOLDOWN = new OldMechanics<>(new AttackCooldownModule());
    public static final OldMechanics<PlayerRegenModule> PLAYER_REGEN = new OldMechanics<>(new PlayerRegenModule());
    public static final OldMechanics<ProjectileKnockbackModule> PROJECTILE_KNOCKBACK = new OldMechanics<>(new ProjectileKnockbackModule());
    public static final OldMechanics<SwordBlockingModule> SWORD_BLOCKING = new OldMechanics<>(new SwordBlockingModule());

    public void enable(VeranyPlugin project) {
        value.setEnabled(true, project);
        Verany.EVENT_REGISTRY.registerListener(value);
    }

    public void disable(VeranyPlugin project) {
        value.setEnabled(false, project);
        Verany.EVENT_REGISTRY.unregisterListener(value);
    }

}
