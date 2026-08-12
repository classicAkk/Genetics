package net.awyvrix.genetics.content.items;

import net.awyvrix.genetics.content.data.custom.GeneValue;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MatrixItem extends Item {
    public MatrixItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(ModDataComponents.MATRIX_SAMPLE)) {
            MatrixSample sample = stack.get(ModDataComponents.MATRIX_SAMPLE);

            if (sample != null) format(tooltipComponents, sample);;
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public static void format(List<Component> tooltipComponents, MatrixSample sample) {
        for (int i = 0; i < sample.genes().size(); i++) {
            GeneValue gene = sample.genes().get(i);
            String type = gene.type().getSerializedName();

            tooltipComponents.add(
                    Component.literal("Gene: ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(String.valueOf(i + 1)).withStyle(ChatFormatting.AQUA))
                            .append(Component.literal(" | ").withStyle(ChatFormatting.GOLD))
                            .append(Component.literal(type).withStyle(ChatFormatting.GREEN))
            );
            tooltipComponents.add(
                    Component.literal("          Purity: ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(gene.purity() + "%").withStyle(ChatFormatting.AQUA))
            );
        }
    }
}