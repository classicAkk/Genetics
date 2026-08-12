package net.awyvrix.genetics.content.items;

import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.content.samples.BloodSample;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SyringeItem extends Item {
    public SyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        BloodSample sample = stack.get(ModDataComponents.BLOOD_SAMPLE);

        if (sample == null) {
            tooltipComponents.add(Component.literal("No Genes Found").withStyle(ChatFormatting.RED));
        } else if (sample.gene() == GeneType.FILLED) {
            tooltipComponents.add(Component.literal("Filled with blood. No special genes").withStyle(ChatFormatting.GREEN));
        }
        else {
            tooltipComponents.add(
                    Component.literal("Gene: ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(sample.gene().getSerializedName()).withStyle(ChatFormatting.GREEN))
            );
            tooltipComponents.add(
                    Component.literal("Purity: ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(sample.purity() + "%").withStyle(ChatFormatting.AQUA))
            );
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}