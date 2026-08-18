package net.awyvrix.genetics.content.inits;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.blocks.centrifuge.CentrifugeBlock;
import net.awyvrix.genetics.content.blocks.cloner.ClonerBlock;
import net.awyvrix.genetics.content.blocks.genPlant.GenPlantBlock;
import net.awyvrix.genetics.content.blocks.petriDish.PetriBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Genetics.MOD_ID);

    public static final DeferredBlock<Block> PETRI_DISH = registerBlock("petri_dish",
            () -> new PetriBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.GLASS).noOcclusion().strength(1)
            ));

    public static final DeferredBlock<Block> GEN_PLANT = registerBlock("gen_plant",
            () -> new GenPlantBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.MOSS).noOcclusion().noCollission()
            ));

    public static final DeferredBlock<Block> CENTRIFUGE = registerBlock("centrifuge",
            () -> new CentrifugeBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.METAL).noOcclusion().strength(1)
            ));

    public static final DeferredBlock<Block> CLONER = registerBlock("cloner",
            () -> new ClonerBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.METAL).noOcclusion().strength(1)
            ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}