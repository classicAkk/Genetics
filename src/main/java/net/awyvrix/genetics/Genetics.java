package net.awyvrix.genetics;

import com.mojang.logging.LogUtils;
import net.awyvrix.genetics.content.commands.ModCommands;
import net.awyvrix.genetics.content.data.ModDataAttachments;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.inits.ModBlockEntities;
import net.awyvrix.genetics.content.inits.ModBlocks;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.registry.GenesManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Genetics.MOD_ID)
public class Genetics {
    public static final String MOD_ID = "genetics";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static GenesManager genesManager = new GenesManager();

    public Genetics(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModDataComponents.COMPONENTS.register(modEventBus);
        ModDataAttachments.ATTACHMENTS.register(modEventBus);

        NeoForge.EVENT_BUS.register(ModCommands.class);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}