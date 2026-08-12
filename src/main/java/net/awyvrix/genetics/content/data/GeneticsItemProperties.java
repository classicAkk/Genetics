package net.awyvrix.genetics.content.data;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.inits.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public final class GeneticsItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.SYRINGE.get(), ResourceLocation.fromNamespaceAndPath(Genetics.MOD_ID, "filled"),
                ((stack, level, entity, seed) -> stack.get(ModDataComponents.BLOOD_SAMPLE) == null ? 0f : 1f));
    }
}
