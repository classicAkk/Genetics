package net.awyvrix.genetics.geneEffects;

import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.content.data.ModDataAttachments;
import net.awyvrix.genetics.content.data.custom.PlayerData;
import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.HashMap;
import java.util.Map;

public class Astrachid implements GeneEffect {

    @Override
    public void apply(ServerPlayer player, int purity) {}

    @Override
    public void tick(ServerPlayer player, int purity) {
        PlayerData data = player.getData(ModDataAttachments.PLAYER_DATA);
        Map<GeneType, Integer> dna = new HashMap<>(data.dna());
        Map<GeneType, Boolean> applied = new HashMap<>(data.applied());

        dna.remove(GeneType.ASTRACHID);
        applied.remove(GeneType.ASTRACHID);

        player.setData(ModDataAttachments.PLAYER_DATA, new PlayerData(dna, applied));
        player.addEffect(new MobEffectInstance(MobEffects.WITHER, 6000, 9, true, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 6000, 9, true, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.HARM, 6000, 0, true, false, false));
        player.displayClientMessage(Component.literal("Ты не достоен принять Высшую Сущность").withStyle(ChatFormatting.RED), false);
    }

    @Override
    public void remove(ServerPlayer player, int purity) {}

    @Override
    public boolean context(ServerPlayer player) {
        MobEffectInstance instance = player.getEffect(MobEffects.ABSORPTION);
        MobEffectInstance instanceBad = player.getEffect(MobEffects.WEAKNESS);

        if (instance != null) return instance.endsWithin(300);
        if (instanceBad != null) return instanceBad.endsWithin(300);
        return true;
    }
}