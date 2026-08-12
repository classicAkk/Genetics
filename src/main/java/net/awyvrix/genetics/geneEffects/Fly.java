package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;

public class Fly implements GeneEffect {
    private int tick;

    @Override
    public void apply(ServerPlayer player, int purity) {
        var attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);

        if (attribute == null) return;
        if (purity >= 100) attribute.setBaseValue(1);
    }

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity <= 70) player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 1, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        var attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);

        if (attribute == null) return;
        if (purity >= 100) attribute.setBaseValue(0);
    }

    @Override
    public boolean context(ServerPlayer player) {
        tick++;
        return tick % 6 == 0;
    }
}