package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WaterSpeed implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {
        if (purity >= 60) {
            var attribute = player.getAttribute(Attributes.SUBMERGED_MINING_SPEED);
            if (attribute != null) attribute.setBaseValue(attribute.getValue() + 0.4);
        }
        if (purity >= 80) {
            var attribute = player.getAttribute(Attributes.SUBMERGED_MINING_SPEED);
            if (attribute != null) attribute.setBaseValue(attribute.getValue() + 0.4);
        }
    }

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity < 30) player.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        if (purity >= 60) {
            var attribute = player.getAttribute(Attributes.SUBMERGED_MINING_SPEED);
            if (attribute != null) attribute.setBaseValue(attribute.getValue() - 0.8);
        }
        if (purity >= 80) {
            var attribute = player.getAttribute(Attributes.SUBMERGED_MINING_SPEED);
            if (attribute != null) attribute.setBaseValue(attribute.getValue() - 0.4);
        }
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.POISON);

        if (instance != null) return instance.endsWithin(300);
        return true;
    }
}