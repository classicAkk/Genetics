package net.awyvrix.genetics.content;

import net.awyvrix.genetics.content.data.*;
import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.registry.GenesRegistry;
import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.content.samples.CellSample;
import net.awyvrix.genetics.content.samples.BloodSample;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Random;

@EventBusSubscriber(modid = Genetics.MOD_ID)
public final class ModEvents {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        Player player = event.getEntity();

        if (event.getTarget() instanceof LivingEntity entity && player.isShiftKeyDown() && !player.getCooldowns().isOnCooldown(ModItems.SYRINGE.get())) {
            ItemStack stack = event.getItemStack();

            if (stack.is(ModItems.SYRINGE)) {
                BloodSample sample = stack.get(ModDataComponents.BLOOD_SAMPLE);
                ItemStack syringe = new ItemStack(ModItems.SYRINGE.get());

                if (sample == null) {
                    if (GenesRegistry.get(entity) == null) {
                        syringe.set(ModDataComponents.BLOOD_SAMPLE, new BloodSample(GeneType.FILLED, 0));
                    } else {
                        syringe.set(ModDataComponents.BLOOD_SAMPLE, new BloodSample(GenesRegistry.get(entity), new Random().nextInt(1, 20)));
                    }
                }
                player.setItemInHand(InteractionHand.MAIN_HAND, syringe);

                if (entity instanceof ServerPlayer targetPlayer) {
                    if (Math.random() <= 0.03) {
                        ItemStack cellStack = new ItemStack(ModItems.CELL.get());
                        cellStack.set(ModDataComponents.CELL_SAMPLE, new CellSample(targetPlayer.getStringUUID(), targetPlayer.getName().getString()));
                        player.addItem(cellStack);
                    }
                }

                player.getCooldowns().addCooldown(ModItems.SYRINGE.get(), 40);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
}
