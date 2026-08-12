package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static net.awyvrix.genetics.registry.EffectRegistry.explosionImmune;
import static net.awyvrix.genetics.registry.EffectRegistry.witherImmune;

public class ExplosionResistance implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 100) if (!explosionImmune.contains(player.getUUID())) explosionImmune.add(player.getUUID());
        if (purity <= 75) player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 600, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        explosionImmune.remove(player.getUUID());
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.DARKNESS);

        if (instance != null) return instance.endsWithin(300);
        return true;
    }
}