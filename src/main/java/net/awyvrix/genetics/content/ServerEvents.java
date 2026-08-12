package net.awyvrix.genetics.content;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static net.awyvrix.genetics.Genetics.MOD_ID;
import static net.awyvrix.genetics.Genetics.genesManager;

@EventBusSubscriber(modid = MOD_ID)
public final class ServerEvents {
    static int tick;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tick++;
        if (tick % 20 == 0) {
            for (ServerLevel level : event.getServer().getAllLevels()) {
                genesManager.tick(level);
                tick = 0;
            }
        }
    }
}
