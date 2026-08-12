package net.awyvrix.genetics.content.items;

import net.awyvrix.genetics.content.samples.CellSample;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CellItem extends Item {
    public CellItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        String owner;
        ChatFormatting color;

        if (stack.has(ModDataComponents.CELL_SAMPLE)) {
            CellSample sample = stack.get(ModDataComponents.CELL_SAMPLE);
            owner = sample.owner();
            color = ChatFormatting.GREEN;
        } else {
            owner = "None";
            color = ChatFormatting.RED;
        }

        tooltipComponents.add(Component.literal("Owner: ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(owner).withStyle(color)));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}