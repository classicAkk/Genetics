package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FireResistance implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {
        var attribute = player.getAttribute(Attributes.BURNING_TIME);

        if (attribute == null) return;
        if (purity >= 75) attribute.setBaseValue(attribute.getValue() - 1);
        if (purity >= 50 && purity < 75) attribute.setBaseValue(attribute.getValue() - 0.5);
    }

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 100) player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 1, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        var attribute = player.getAttribute(Attributes.BURNING_TIME);

        if (attribute == null) return;
        if (purity >= 75) attribute.setBaseValue(attribute.getValue() + 1);
        if (purity >= 50 && purity < 75) attribute.setBaseValue(attribute.getValue() + 0.5);
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.FIRE_RESISTANCE);

        if (instance != null) return instance.endsWithin(300);
        return true;
    }
}