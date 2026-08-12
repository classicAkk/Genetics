package net.awyvrix.genetics.content.commands.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.awyvrix.genetics.content.commands.CommandUtils;
import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.content.samples.BloodSample;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SetGeneCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return literal("set")
                .then(argument("gene", StringArgumentType.word())
                        .suggests((context, builder) ->
                                SharedSuggestionProvider.suggest(Arrays.stream(
                                        GeneType.values()).map(GeneType::getLowerCaseName), builder
                                )
                        )
                        .then(argument("purity", IntegerArgumentType.integer(-100, 100))
                                .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayer();
                                            GeneType gene = CommandUtils.getEnum(ctx, "gene", GeneType.class);
                                            int purity = IntegerArgumentType.getInteger(ctx, "purity");

                                            if (player == null) return 1;
                                            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

                                            if (stack.is(ModItems.SYRINGE.get())) {
                                                stack.set(ModDataComponents.BLOOD_SAMPLE, new BloodSample(gene, purity));
                                            }
                                            CommandUtils.success(ctx, Component.literal("Gene successfully modified"));
                                            return 1;
                                        }
                                )
                        )
                );
    }
}