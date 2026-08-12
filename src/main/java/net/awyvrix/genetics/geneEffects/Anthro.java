package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.awyvrix.genetics.registry.EffectRegistry.anthros;

public class Anthro implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {
        if (purity >= 100) {
            var maxHp = player.getAttribute(Attributes.MAX_HEALTH);
            var maxDmg = player.getAttribute(Attributes.ATTACK_DAMAGE);
            var maxSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            var sneakSpeed = player.getAttribute(Attributes.SNEAKING_SPEED);

            if (maxHp != null) maxHp.setBaseValue(maxHp.getValue() + 20.0);
            if (maxDmg != null) maxDmg.setBaseValue(maxDmg.getValue() + 3.0);
            if (maxSpeed != null) maxSpeed.setBaseValue(maxSpeed.getValue() + 0.08);
            if (sneakSpeed != null) sneakSpeed.setBaseValue(sneakSpeed.getValue() + 0.2);
        }
    }

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 100) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1, true, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0, true, false, false));
            if (!anthros.contains(player.getUUID())) anthros.add(player.getUUID());
        }
        if (purity < 100) player.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        if (purity >= 100) {
            var maxHp = player.getAttribute(Attributes.MAX_HEALTH);
            var maxDmg = player.getAttribute(Attributes.ATTACK_DAMAGE);
            var maxSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            var sneakSpeed = player.getAttribute(Attributes.SNEAKING_SPEED);

            if (maxHp != null) maxHp.setBaseValue(maxHp.getValue() - 20.0);
            if (maxDmg != null) maxDmg.setBaseValue(maxDmg.getValue() - 3.0);
            if (maxSpeed != null) maxSpeed.setBaseValue(maxSpeed.getValue() - 0.08);
            if (sneakSpeed != null) sneakSpeed.setBaseValue(sneakSpeed.getValue() - 0.2);
            anthros.remove(player.getUUID());
        }
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.REGENERATION);
        MobEffectInstance instance2 = player.getEffect(MobEffects.DAMAGE_RESISTANCE);

        if (instance != null) return instance.endsWithin(300);
        if (instance2 != null) return instance2.endsWithin(300);
        return true;
    }
}