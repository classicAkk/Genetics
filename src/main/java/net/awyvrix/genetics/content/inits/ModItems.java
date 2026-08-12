package net.awyvrix.genetics.content.inits;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.items.CellItem;
import net.awyvrix.genetics.content.items.InjectSyringeItem;
import net.awyvrix.genetics.content.items.MatrixItem;
import net.awyvrix.genetics.content.items.SyringeItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Genetics.MOD_ID);

    public static final DeferredItem<Item> SYRINGE = ITEMS.register("syringe",
            () -> new SyringeItem(new Item.Properties()
                    .stacksTo(1)
            ));

    public static final DeferredItem<Item> INJECT_SYRINGE = ITEMS.register("inject_syringe",
            () -> new InjectSyringeItem(new Item.Properties()
                    .stacksTo(1)
            ));

    public static final DeferredItem<Item> DNA = ITEMS.register("dna",
            () -> new Item(new Item.Properties()
                    .stacksTo(16)
            ));

    public static final DeferredItem<Item> DNA_MATRIX = ITEMS.register("dna_matrix",
            () -> new MatrixItem(new Item.Properties()
                    .stacksTo(1)
            ));

    public static final DeferredItem<Item> CELL = ITEMS.register("cell",
            () -> new CellItem(new Item.Properties()
                    .stacksTo(16)
            ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}