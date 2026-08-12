package net.awyvrix.genetics.content.items;

import net.awyvrix.genetics.content.data.*;
import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.content.data.custom.GeneValue;
import net.awyvrix.genetics.content.data.custom.PlayerData;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.*;

import static net.awyvrix.genetics.Genetics.genesManager;

public class InjectSyringeItem extends Item {
    public InjectSyringeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;
        Player player = context.getPlayer();

        if (player.isShiftKeyDown()) return InteractionResult.SUCCESS;
        ItemStack offStack = player.getItemInHand(InteractionHand.OFF_HAND);

        if (offStack.is(ModItems.CELL.get()) && offStack.has(ModDataComponents.CELL_SAMPLE)) {
            if (offStack.get(ModDataComponents.CELL_SAMPLE).id().equals(player.getStringUUID())) {
                if (!player.getCooldowns().isOnCooldown(ModItems.INJECT_SYRINGE.get()) && player instanceof ServerPlayer serverPlayer) {
                    MatrixSample sample = context.getItemInHand().get(ModDataComponents.MATRIX_SAMPLE);
                    offStack.shrink(1);

                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.SYRINGE.get()));
                    player.getCooldowns().addCooldown(ModItems.SYRINGE.get(), 120);
                    player.getCooldowns().addCooldown(ModItems.INJECT_SYRINGE.get(), 120);

                    if (sample == null) return InteractionResult.SUCCESS;
                    PlayerData data = player.getData(ModDataAttachments.PLAYER_DATA);

                    if (data.dna() == null) return InteractionResult.SUCCESS;
                    Map<GeneType, Integer> dna = new HashMap<>(data.dna());
                    Map<GeneType, Boolean> applied = new HashMap<>(data.applied());

                    for (GeneValue gene : sample.genes()) {
                        if (gene.purity() < 0) {
                            dna.remove(gene.type());
                            applied.put(gene.type(), false);
                            genesManager.removeTick(serverPlayer, gene.type());
                        } else {
                            if (dna.containsKey(gene.type())) {
                                dna.replace(gene.type(), gene.purity());
                            } else {
                                dna.put(gene.type(), gene.purity());
                            }
                        }
                    }

                    player.setData(ModDataAttachments.PLAYER_DATA, new PlayerData(dna, applied));
                }
            } else {
                player.displayClientMessage(Component.literal("Wrong player cell").withStyle(ChatFormatting.RED), false);
            }
        } else {
            player.displayClientMessage(Component.literal("Cell required").withStyle(ChatFormatting.RED), false);
        }
        return InteractionResult.SUCCESS;
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