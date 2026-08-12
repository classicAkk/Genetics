package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class JumpBoost implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 85) player.addEffect(new MobEffectInstance(MobEffects.JUMP, 600, 1, true, false, false));
        if (purity >= 50) player.addEffect(new MobEffectInstance(MobEffects.JUMP, 600, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        player.removeEffect(MobEffects.NIGHT_VISION);
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.JUMP);

        if (instance != null) return instance.endsWithin(300);
        return true;
    }
}