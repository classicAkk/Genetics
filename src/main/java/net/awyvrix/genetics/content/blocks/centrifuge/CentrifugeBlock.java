package net.awyvrix.genetics.content.blocks.centrifuge;

import net.awyvrix.genetics.content.data.custom.GeneValue;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.content.samples.BloodSample;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CentrifugeBlock extends Block implements EntityBlock {
    public static final EnumProperty<CentrifugeState> STATE = EnumProperty.create("state", CentrifugeState.class);
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(8.5, 4, 9, 9.5, 6, 10),
            Block.box(5, 0, 5, 11, 2, 11),
            Block.box(6, 2, 6, 10, 4, 10),
            Block.box(6.5, 4, 6, 7.5, 6, 7),
            Block.box(6, 4, 7.5, 7, 6, 8.5),
            Block.box(8.5, 4, 6, 9.5, 6, 7),
            Block.box(9, 4, 7.5, 10, 6, 8.5),
            Block.box(6.5, 4, 9, 7.5, 6, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public CentrifugeBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.defaultBlockState().setValue(STATE, CentrifugeState.EMPTY));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CentrifugeBE(pPos, pState);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (hand != InteractionHand.MAIN_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (stack.is(ModItems.SYRINGE)) {
            if (stack.has(ModDataComponents.BLOOD_SAMPLE)) {
                nextStage(level, pos, state, stack, serverPlayer);
            } else {
                backStage(level, pos, state, serverPlayer);
            }
        }

        if (stack.is(Items.AMETHYST_SHARD)) {
            if (level.getBlockEntity(pos) instanceof CentrifugeBE centrifuge) {
                if (centrifuge.matrix.genes().isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                RandomSource random = level.random;
                stack.shrink(1);

                ItemStack matrix = new ItemStack(ModItems.DNA_MATRIX.get());
                List<GeneValue> newMatrix = new ArrayList<>();

                for (GeneValue gene : centrifuge.matrix.genes()) {
                    int purity = gene.purity();

                    int newPurity = purity < 35
                            ? Math.min(purity + random.nextInt(0, 11), 35)
                            : purity;
                    newMatrix.add(new GeneValue(gene.type(), newPurity));
                }

                matrix.set(ModDataComponents.MATRIX_SAMPLE, new MatrixSample(newMatrix));
                player.addItem(matrix);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.WAX_ON, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 15, 0.2, 0.2, 0.2, 0.02);
                }
                level.playSound(null, pos, SoundEvents.VAULT_PLACE, SoundSource.BLOCKS, 0.8f, 1.0f);
                level.setBlock(pos, state.setValue(CentrifugeBlock.STATE, CentrifugeState.EMPTY), 3);
                centrifuge.matrix = new MatrixSample(new ArrayList<>());
                centrifuge.setChanged();
            }
        }

        if (stack.is(ModItems.DNA_MATRIX) && stack.has(ModDataComponents.MATRIX_SAMPLE)) {
            if (state.getValue(CentrifugeBlock.STATE) == CentrifugeState.EMPTY) {
                if (level.getBlockEntity(pos) instanceof CentrifugeBE centrifuge) {
                    MatrixSample matrix = stack.get(ModDataComponents.MATRIX_SAMPLE);
                    stack.shrink(1);

                    centrifuge.matrix = new MatrixSample(new ArrayList<>(matrix.genes()));
                    centrifuge.setChanged();
                    level.playSound(null, pos, SoundEvents.VAULT_DEACTIVATE, SoundSource.BLOCKS, 0.8f, 1.0f);
                    level.setBlock(pos, state.setValue(CentrifugeBlock.STATE, CentrifugeState.values()[matrix.genes().size()]), 3);
                }
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    private void nextStage(Level level, BlockPos pos, BlockState state, ItemStack stack, ServerPlayer player) {
        if (level.getBlockEntity(pos) instanceof CentrifugeBE centrifuge) {
            BloodSample sample = stack.get(ModDataComponents.BLOOD_SAMPLE);
            int index = state.getValue(CentrifugeBlock.STATE).ordinal();

            if (index+1 > CentrifugeState.values().length) return;
            CentrifugeState newState = CentrifugeState.values()[index+1];

            ArrayList<GeneValue> list = new ArrayList<>(centrifuge.matrix.genes());
            list.add(new GeneValue(sample.gene(), sample.purity()));
            centrifuge.matrix = new MatrixSample(list);
            centrifuge.setChanged();

            ItemStack newStack = new ItemStack(ModItems.SYRINGE.get());

            player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
            level.setBlock(pos, state.setValue(CentrifugeBlock.STATE, newState), 3);
            level.playSound(null, pos, SoundEvents.VAULT_INSERT_ITEM, SoundSource.BLOCKS, 0.8f, 1.0f);
        }
    }

    private void backStage(Level level, BlockPos pos, BlockState state, ServerPlayer player) {
        if (level.getBlockEntity(pos) instanceof CentrifugeBE centrifuge) {
            int index = state.getValue(CentrifugeBlock.STATE).ordinal();

            if (index == 0) return;
            CentrifugeState newState = CentrifugeState.values()[index-1];
            GeneValue gene = centrifuge.matrix.genes().getLast();

            ArrayList<GeneValue> list = new ArrayList<>(centrifuge.matrix.genes());
            list.removeLast();
            centrifuge.matrix = new MatrixSample(list);
            centrifuge.setChanged();

            ItemStack newStack = new ItemStack(ModItems.SYRINGE.get());
            newStack.set(ModDataComponents.BLOOD_SAMPLE, new BloodSample(gene.type(), gene.purity()));

            player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
            level.setBlock(pos, state.setValue(CentrifugeBlock.STATE, newState), 3);
            level.playSound(null, pos, SoundEvents.VAULT_EJECT_ITEM, SoundSource.BLOCKS, 0.8f, 1.0f);
        }
    }
}
