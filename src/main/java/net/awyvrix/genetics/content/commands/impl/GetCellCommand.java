package net.awyvrix.genetics.content.commands.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.awyvrix.genetics.content.commands.CommandUtils;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.content.samples.CellSample;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.commands.Commands.literal;

public class GetCellCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("get_cell")
                .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();

                            if (player == null) return 1;
                            ItemStack stack = new ItemStack(ModItems.CELL.get());
                            stack.set(ModDataComponents.CELL_SAMPLE, new CellSample(player.getStringUUID(), player.getName().getString()));
                            player.addItem(stack);

                            CommandUtils.success(ctx, Component.literal("Gene successfully modified"));
                            return 1;
                        }
                );
    }
}