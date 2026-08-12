package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ConduitPower implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 90) player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 600, 0, true, false, false));
        if (purity < 85) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        player.removeEffect(MobEffects.CONDUIT_POWER);
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.CONDUIT_POWER);
        MobEffectInstance instanceBad = player.getEffect(MobEffects.WEAKNESS);

        if (instance != null) return instance.endsWithin(300);
        if (instanceBad != null) return instanceBad.endsWithin(300);
        return true;
    }
}