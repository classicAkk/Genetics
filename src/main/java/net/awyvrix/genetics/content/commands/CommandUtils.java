package net.awyvrix.genetics.content.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class CommandUtils {

    public static void success(CommandContext<CommandSourceStack> ctx, String text) {
        ctx.getSource().sendSuccess(() -> Component.literal(text).withStyle(ChatFormatting.GREEN), false);
    }

    public static void success(CommandContext<CommandSourceStack> ctx, Component text) {
        ctx.getSource().sendSuccess(() -> text, false);
    }

    public static void error(CommandContext<CommandSourceStack> ctx, String text) {
        ctx.getSource().sendFailure(Component.literal(text).withStyle(ChatFormatting.RED));
    }

    public static <E extends Enum<E>> E getEnum(CommandContext<CommandSourceStack> ctx, String arg, Class<E> enumClass) {
        return Enum.valueOf(enumClass, StringArgumentType.getString(ctx, arg).toUpperCase());
    }
}