package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class NightVision implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity > 85) player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0, true, false, false));
        if (purity < 50) player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        player.removeEffect(MobEffects.NIGHT_VISION);
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.NIGHT_VISION);
        MobEffectInstance instanceBad = player.getEffect(MobEffects.BLINDNESS);

        if (instance != null) return instance.endsWithin(300);
        if (instanceBad != null) return instanceBad.endsWithin(300);
        return true;
    }
}