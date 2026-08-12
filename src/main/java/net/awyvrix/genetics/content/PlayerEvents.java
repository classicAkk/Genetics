package net.awyvrix.genetics.content;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import static net.awyvrix.genetics.Genetics.MOD_ID;
import static net.awyvrix.genetics.registry.EffectRegistry.*;

@EventBusSubscriber(modid = MOD_ID)
public final class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (anthros.contains(player.getUUID())) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onGetDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (entity instanceof ServerPlayer player && source.is(DamageTypes.WITHER)) {
            if (witherImmune.contains(player.getUUID())) event.setCanceled(true);
        }
        if (entity instanceof ServerPlayer player && source.is(DamageTypes.EXPLOSION)) {
            if (explosionImmune.contains(player.getUUID())) event.setCanceled(true);
        }
        if (entity instanceof ServerPlayer player && source.is(DamageTypes.DRAGON_BREATH)) {
            if (dragon.contains(player.getUUID())) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDealDamage(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();

        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            if (wither.contains(player.getUUID())) victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 5, 1));
            if (anthros.contains(player.getUUID())) player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0));
        }
    }
}
