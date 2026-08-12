package net.awyvrix.genetics.content.inits;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.blocks.centrifuge.CentrifugeBE;
import net.awyvrix.genetics.content.blocks.genPlant.GenPlantBE;
import net.awyvrix.genetics.content.blocks.petriDish.PetriBE;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Genetics.MOD_ID);

    public static final Supplier<BlockEntityType<PetriBE>> PETRI =
            BLOCK_ENTITIES.register("petri",
                    ()-> BlockEntityType.Builder.of(PetriBE::new, ModBlocks.PETRI_DISH.get()).build(null));

    public static final Supplier<BlockEntityType<CentrifugeBE>> CENTRIFUGE =
            BLOCK_ENTITIES.register("centrifuge",
                    ()-> BlockEntityType.Builder.of(CentrifugeBE::new, ModBlocks.CENTRIFUGE.get()).build(null));

    public static final Supplier<BlockEntityType<GenPlantBE>> GEN_PLANT =
            BLOCK_ENTITIES.register("gen_plant",
                    ()-> BlockEntityType.Builder.of(GenPlantBE::new, ModBlocks.GEN_PLANT.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}