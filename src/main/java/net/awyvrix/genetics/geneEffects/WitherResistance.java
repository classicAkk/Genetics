package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.awyvrix.genetics.registry.EffectRegistry.witherImmune;

public class WitherResistance implements GeneEffect {
    private int tick;

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 100) if (!witherImmune.contains(player.getUUID())) witherImmune.add(player.getUUID());
        if (purity <= 75) player.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        witherImmune.remove(player.getUUID());
    }

    @Override
    public boolean context(ServerPlayer player) {
        tick++;
        return tick % 15 == 0;
    }
}