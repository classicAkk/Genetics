package net.awyvrix.genetics.registry;

import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.content.data.ModDataAttachments;
import net.awyvrix.genetics.content.data.custom.PlayerData;
import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public final class GenesManager {
    public void tick(Level level) {
        for (Player player : level.players()) {
            if (player instanceof ServerPlayer serverPlayer) {
                Map<GeneType, Integer> dna = serverPlayer.getData(ModDataAttachments.PLAYER_DATA).dna();
                Map<GeneType, Boolean> applied = serverPlayer.getData(ModDataAttachments.PLAYER_DATA).applied();

                if (dna == null || dna.isEmpty()) continue;
                for (GeneType type : dna.keySet()) {
                    GeneEffect effect = EffectRegistry.get(type);

                    if (effect == null) continue;
                    if (applied.get(type) == null || !applied.get(type)) {
                        effect.apply(serverPlayer, dna.get(type));

                        Map<GeneType, Boolean> newMap = new HashMap<>(applied);
                        newMap.put(type, true);
                        serverPlayer.setData(ModDataAttachments.PLAYER_DATA, new PlayerData(dna, newMap));
                    }
                    if (effect.context(serverPlayer)) {
                        effect.tick(serverPlayer, dna.get(type));
                    }
                }
            }
        }
    }

    public void removeTick(ServerPlayer player, GeneType type) {
        GeneEffect effect = EffectRegistry.get(type);

        if (effect == null) return;
        effect.remove(player, player.getData(ModDataAttachments.PLAYER_DATA).dna().get(type));
    }
}