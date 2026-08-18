package net.awyvrix.genetics.content.blocks.petriDish;

import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.content.samples.CellSample;
import net.awyvrix.genetics.util.TickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class PetriBlock extends Block implements EntityBlock {
    public static final EnumProperty<PetriState> STATE = EnumProperty.create("state", PetriState.class);
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 0, 5, 11, 1, 11),
            Block.box(5, 0, 11, 11, 2, 12),
            Block.box(5, 0, 4, 11, 2, 5),
            Block.box(11, 0, 4, 12, 2, 12),
            Block.box(4, 0, 4, 5, 2, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public PetriBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.defaultBlockState().setValue(STATE, PetriState.EMPTY));
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

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PetriBE(pPos, pState);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (hand != InteractionHand.MAIN_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (state.getValue(PetriBlock.STATE) == PetriState.EMPTY) {
            if (stack.is(Items.MILK_BUCKET)) level.setBlock(pos, state.setValue(PetriBlock.STATE, PetriState.STAGE1), 3);
            level.playLocalSound(pos, SoundEvents.COW_MILK, SoundSource.BLOCKS, 1.0f, 1.0f, true);
        }
        if (state.getValue(PetriBlock.STATE) == PetriState.STAGE1) {
            if (stack.is(Items.BEEF)) level.setBlock(pos, state.setValue(PetriBlock.STATE, PetriState.STAGE2), 3);
            level.playLocalSound(pos, SoundEvents.CANDLE_FALL, SoundSource.BLOCKS, 1.0f, 1.0f, true);
        }
        if (state.getValue(PetriBlock.STATE) == PetriState.STAGE2) {
            if (stack.is(ModItems.SYRINGE.get()) && stack.has(ModDataComponents.BLOOD_SAMPLE)) {
                if (stack.has(ModDataComponents.BLOOD_SAMPLE)) {
                    level.setBlock(pos, state.setValue(PetriBlock.STATE, PetriState.READY), 3);
                    level.playLocalSound(pos, SoundEvents.COBWEB_HIT, SoundSource.BLOCKS, 1.0f, 1.0f, true);
                }
            }
        }

        if ((level.getBlockEntity(pos) instanceof PetriBE petri)) {
            if (state.getValue(PetriBlock.STATE) == PetriState.CELLS || state.getValue(PetriBlock.STATE) == PetriState.READY) {
                ItemStack offStack = player.getItemInHand(InteractionHand.OFF_HAND);
                if (stack.has(ModDataComponents.CELL_SAMPLE) && !offStack.has(ModDataComponents.MATRIX_SAMPLE)) {
                    petri.cells++;
                    petri.id = stack.get(ModDataComponents.CELL_SAMPLE).id();
                    petri.owner = stack.get(ModDataComponents.CELL_SAMPLE).owner();
                    stack.shrink(1);

                    if (state.getValue(PetriBlock.STATE) == PetriState.READY) level.setBlock(pos, state.setValue(PetriBlock.STATE, PetriState.CELLS), 3);
                    return ItemInteractionResult.SUCCESS;
                }
                if (stack.isEmpty()) {
                    ItemStack cells = new ItemStack(ModItems.CELL.get(), petri.cells);
                    cells.set(ModDataComponents.CELL_SAMPLE, new CellSample(petri.id, petri.owner));
                    player.addItem(cells);
                    petri.cells = 0;
                    petri.owner = null;
                    petri.id = null;

                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type){
        return TickableBE.getTickerHelper(level);
    }
}
