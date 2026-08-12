package net.awyvrix.genetics.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.awyvrix.genetics.content.commands.impl.GetCellCommand;
import net.awyvrix.genetics.content.commands.impl.SetGeneCommand;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static net.minecraft.commands.Commands.literal;

public final class ModCommands {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                literal("genetics")
                        .requires(src -> src.hasPermission(2))
                        .then(SetGeneCommand.register())
                        .then(GetCellCommand.register())
        );
    }
}
