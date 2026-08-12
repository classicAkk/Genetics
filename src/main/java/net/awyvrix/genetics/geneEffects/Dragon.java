package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import static net.awyvrix.genetics.registry.EffectRegistry.dragon;
import static net.awyvrix.genetics.registry.EffectRegistry.wither;

public class Dragon implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        if (purity >= 100) if (!dragon.contains(player.getUUID())) dragon.add(player.getUUID());
        if (purity <= 75) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, false, false));
    }

    @Override
    public void remove(ServerPlayer player, int purity) {
        dragon.remove(player.getUUID());
    }

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.WEAKNESS);

        if (instance != null) return instance.endsWithin(300);
        return true;
    }
}