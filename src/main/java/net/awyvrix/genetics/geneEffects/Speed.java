package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class Speed implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 100) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1, true, false, false));
        if (purity >= 65) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, true, false, false));
        if (purity < 35) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        player.removeEffect(MobEffects.MOVEMENT_SPEED);
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.MOVEMENT_SPEED);
        MobEffectInstance instanceBad = player.getEffect(MobEffects.MOVEMENT_SLOWDOWN);

        if (instance != null) return instance.endsWithin(300);
        if (instanceBad != null) return instanceBad.endsWithin(300);
        return true;
    }
}