package net.awyvrix.genetics.content.blocks.cloner;

import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.data.custom.GeneValue;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.content.samples.BloodSample;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClonerBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ClonerState> STATE = EnumProperty.create("state", ClonerState.class);
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(4, 0, 4, 12, 2, 12),
            Block.box(5, 2, 5, 11, 4, 11)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();


    public ClonerBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.defaultBlockState().setValue(STATE, ClonerState.EMPTY).setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(STATE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!level.getBlockState(pos.below()).isSolid()) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ClonerBE(pPos, pState);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (hand != InteractionHand.MAIN_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (stack.has(ModDataComponents.MATRIX_SAMPLE)) {
            nextStage(level, pos, state, stack, serverPlayer);
        } else if (stack.isEmpty()) {
            backStage(level, pos, state, serverPlayer);
        }

        if (stack.is(Items.AMETHYST_SHARD)) {
            if (level.getBlockEntity(pos) instanceof ClonerBE cloner) {
                if (cloner.sample == null || cloner.matrix == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                stack.shrink(1);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.WAX_ON, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 15, 0.2, 0.2, 0.2, 0.02);
                }
                level.playSound(null, pos, SoundEvents.VAULT_PLACE, SoundSource.BLOCKS, 0.8f, 1.0f);
                cloner.matrix = cloner.sample;
                cloner.setChanged();
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    private void nextStage(Level level, BlockPos pos, BlockState state, ItemStack stack, ServerPlayer player) {
        if (level.getBlockEntity(pos) instanceof ClonerBE cloner) {
            MatrixSample sample = stack.get(ModDataComponents.MATRIX_SAMPLE);
            stack.shrink(1);

            if (state.getValue(ClonerBlock.STATE) == ClonerState.EMPTY) {
                cloner.sample = sample;
                level.setBlock(pos, state.setValue(ClonerBlock.STATE, ClonerState.STAGE1), 3);
            }

            if (state.getValue(ClonerBlock.STATE) == ClonerState.STAGE1) {
                cloner.matrix = sample;
                level.setBlock(pos, state.setValue(ClonerBlock.STATE, ClonerState.STAGE2), 3);
            }

            level.playSound(null, pos, SoundEvents.VAULT_INSERT_ITEM, SoundSource.BLOCKS, 0.8f, 1.0f);
        }
    }

    private void backStage(Level level, BlockPos pos, BlockState state, ServerPlayer player) {
        if (level.getBlockEntity(pos) instanceof ClonerBE cloner) {
            ItemStack stack = new ItemStack(ModItems.DNA_MATRIX.get());

            if (state.getValue(ClonerBlock.STATE) == ClonerState.STAGE2) {
                stack.set(ModDataComponents.MATRIX_SAMPLE, cloner.matrix);
                cloner.matrix = null;
                level.setBlock(pos, state.setValue(ClonerBlock.STATE, ClonerState.STAGE1), 3);
            }

            if (state.getValue(ClonerBlock.STATE) == ClonerState.STAGE1) {
                stack.set(ModDataComponents.MATRIX_SAMPLE, cloner.sample);
                cloner.sample = null;
                level.setBlock(pos, state.setValue(ClonerBlock.STATE, ClonerState.EMPTY), 3);
            }

            if (state.getValue(ClonerBlock.STATE) != ClonerState.EMPTY) {
                player.addItem(stack);
                level.playSound(null, pos, SoundEvents.VAULT_INSERT_ITEM, SoundSource.BLOCKS, 0.8f, 1.0f);
            }
        }
    }
}