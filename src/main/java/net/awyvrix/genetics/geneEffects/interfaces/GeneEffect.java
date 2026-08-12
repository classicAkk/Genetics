package net.awyvrix.genetics.geneEffects.interfaces;

import net.minecraft.server.level.ServerPlayer;

public interface GeneEffect {
    void apply(ServerPlayer player, int purity);
    void tick(ServerPlayer player, int purity);
    void remove(ServerPlayer player, int purity);
    boolean context(ServerPlayer player);
}
