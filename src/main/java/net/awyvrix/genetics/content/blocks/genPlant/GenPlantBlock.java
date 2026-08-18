package net.awyvrix.genetics.content.blocks.genPlant;

import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.content.data.custom.GeneValue;
import net.awyvrix.genetics.content.inits.ModItems;
import net.awyvrix.genetics.registry.MergeRegistry;
import net.awyvrix.genetics.content.data.ModDataComponents;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.awyvrix.genetics.util.TickableBE;
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
import org.apache.logging.log4j.core.pattern.NotANumber;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class GenPlantBlock extends Block implements EntityBlock {
    public static final EnumProperty<PlantState> STATE = EnumProperty.create("state", PlantState.class);
    public static final VoxelShape STAGE0 = Stream.of(
            Block.box(6, 0, 6, 10, 5, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape READY = Stream.of(
            Block.box(3, 0, 3, 13, 16, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public GenPlantBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.defaultBlockState().setValue(STATE, PlantState.STAGE0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (state.getValue(GenPlantBlock.STATE) == PlantState.STAGE0) return STAGE0;
        return READY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos().below()).getBlock() == Blocks.GRASS_BLOCK) return this.defaultBlockState();
        return null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() == Blocks.GRASS_BLOCK;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (level.getBlockState(pos.below()).getBlock() != Blocks.GRASS_BLOCK) {
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
        return new GenPlantBE(pPos, pState);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (hand != InteractionHand.MAIN_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (stack.is(Items.BONE_MEAL) && state.getValue(GenPlantBlock.STATE) == PlantState.STAGE0) {
            level.setBlock(pos, state.setValue(GenPlantBlock.STATE, PlantState.STAGE1), 3);
            level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            stack.shrink(1);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 20, 0.2, 0.4, 0.2, 0.02);
            }
        }

        if (stack.has(ModDataComponents.MATRIX_SAMPLE) && state.getValue(GenPlantBlock.STATE) == PlantState.STAGE1) {
            if (level.getBlockEntity(pos) instanceof GenPlantBE plant) {
                if (stack.is(ModItems.INJECT_SYRINGE.get())) {
                    plant.gene = stack.get(ModDataComponents.MATRIX_SAMPLE);
                    plant.dirty = true;

                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.GLOW, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 20, 0.2, 0.4, 0.2, 0);
                    }
                } else {
                    plant.sample = stack.get(ModDataComponents.MATRIX_SAMPLE);
                    level.setBlock(pos, state.setValue(GenPlantBlock.STATE, PlantState.LOADED), 3);

                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.WAX_ON, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 20, 0.2, 0.4, 0.2, 0);
                    }
                }
                if (stack.is(ModItems.INJECT_SYRINGE)) player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.SYRINGE.get()));
                if (stack.is(ModItems.DNA_MATRIX)) stack.shrink(1);
                plant.setChanged();
            }
        }

        if (stack.isEmpty() && state.getValue(GenPlantBlock.STATE) == PlantState.LOADED) {
            if (level.getBlockEntity(pos) instanceof GenPlantBE plant) {
                ItemStack matrix = new ItemStack(ModItems.DNA_MATRIX.get());
                matrix.set(ModDataComponents.MATRIX_SAMPLE, plant.sample);

                plant.sample = null;
                plant.setChanged();

                level.setBlock(pos, state.setValue(GenPlantBlock.STATE, PlantState.STAGE1), 3);
                player.setItemInHand(InteractionHand.MAIN_HAND, matrix);
            }
        }

        if (stack.is(ModItems.SYRINGE) && (state.getValue(GenPlantBlock.STATE) == PlantState.STAGE1 || state.getValue(GenPlantBlock.STATE) == PlantState.LOADED)) {
            if (stack.has(ModDataComponents.BLOOD_SAMPLE)) {
                RandomSource random = level.getRandom();
                List<MatrixSample> matrixSamples = new ArrayList<>();

                BlockEntity plant1 = level.getBlockEntity(pos.north().west());
                BlockEntity plant2 = level.getBlockEntity(pos.north().east());

                BlockEntity plant3 = level.getBlockEntity(pos.south().west());
                BlockEntity plant4 = level.getBlockEntity(pos.south().east());

                BlockEntity targetPlant = level.getBlockEntity(pos);

                List<Double> speed = new ArrayList<>();
                List<Double> strength = new ArrayList<>();

                if (plant1 instanceof GenPlantBE plantBE1 && plantBE1.sample != null) preparePlant(matrixSamples, speed, strength, plantBE1, level);
                if (plant2 instanceof GenPlantBE plantBE2 && plantBE2.sample != null) preparePlant(matrixSamples, speed, strength, plantBE2, level);
                if (plant3 instanceof GenPlantBE plantBE3 && plantBE3.sample != null) preparePlant(matrixSamples, speed, strength, plantBE3, level);
                if (plant4 instanceof GenPlantBE plantBE4 && plantBE4.sample != null) preparePlant(matrixSamples, speed, strength, plantBE4, level);
                if (!matrixSamples.isEmpty() && targetPlant instanceof GenPlantBE targetBE && targetBE.sample == null) {
                    double str = strength.stream().mapToDouble(Double::doubleValue).average().orElse(1.0) * strength.size() / 4;
                    double spd = speed.stream().mapToDouble(Double::doubleValue).average().orElse(1.0) * speed.size() / 4;

                    if (speed.isEmpty()) spd = 1;
                    if (strength.isEmpty()) str = 1;
                    Map<Integer, List<GeneValue>> genes = new HashMap<>();
                    List<GeneValue> geneRow = new ArrayList<>();
                    List<GeneType> genesRow = new ArrayList<>();
                    List<GeneValue> result = new ArrayList<>();

                    for (MatrixSample sample : matrixSamples) {
                        List<GeneValue> values = sample.genes();

                        for (int i = 0; i < values.size(); i++) {
                            GeneValue gene = values.get(i);
                            GeneType type = gene.type();
                            genes.computeIfAbsent(i, k -> new ArrayList<>());

                            if (genesRow.contains(gene.type())) {
                                int min = gene.purity();
                                int max = geneRow.get(varToNum(geneRow, gene)).purity() + matrixSamples.size();
                                double total = randomBonus(min, max, matrixSamples.size() * str, random);

                                GeneValue val = new GeneValue(type, (int) Math.round(total));
                                genes.get(i).add(val);
                                geneRow.add(val);
                            } else {
                                double purity = randomBonus(gene.purity(), gene.purity(), matrixSamples.size() * str, random);

                                GeneValue val = new GeneValue(type, (int) Math.round(purity));
                                genes.get(i).add(val);
                                geneRow.add(val);
                            }
                            genesRow.add(type);
                        }
                    }

                    for (int i = 0; i < genes.size(); i++) {
                        List<GeneValue> row = genes.get(i);
                        Set<GeneType> mergeSet = new HashSet<>();

                        for (GeneValue gene : row) {
                            mergeSet.add(gene.type());
                        }
                        GeneType merge =  MergeRegistry.get(mergeSet);

                        if (merge == null) {
                            int var = random.nextInt(0, mergeSet.size());
                            GeneType type = mergeSet.stream().toList().get(var);

                            result.add(new GeneValue(type, row.get(var).purity()));
                        } else {
                            result.add(new GeneValue(merge, min(row)));
                        }
                    }

                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.SYRINGE.get()));
                    level.setBlock(pos, state.setValue(GenPlantBlock.STATE, PlantState.GROW), 3);
                    level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0f, 1.0f);

                    targetBE.target = new MatrixSample(result);
                    targetBE.growTimer = (result.size() + sum(result) * 20) / spd;
                }
            } else {
                if (level.getBlockEntity(pos) instanceof GenPlantBE genPlantBE && genPlantBE.sample != null) {
                    ItemStack syringe = new ItemStack(ModItems.INJECT_SYRINGE.get());
                    syringe.set(ModDataComponents.MATRIX_SAMPLE, genPlantBE.sample);
                    player.setItemInHand(InteractionHand.MAIN_HAND, syringe);

                    genPlantBE.sample = null;
                    genPlantBE.setChanged();
                    level.setBlock(pos, state.setValue(GenPlantBlock.STATE, PlantState.STAGE1), 3);
                }
            }
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.WAX_ON, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 20, 0.2, 0.4, 0.2, 0);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    public int min(List<GeneValue> lst) {
        int min = Integer.MAX_VALUE;
        for (GeneValue var : lst) {
            int purity = var.purity();
            if (purity < min) min = purity;
        }
        return min;
    }

    public int random(int a, int b, RandomSource random) {
        b+=1;
        if (a == b) return a;
        if (a > b) {
            return random.nextInt(b, a);
        } else {
            return random.nextInt(a, b);
        }
    }

    public double randomBonus(int a, int b, double bonus, RandomSource random) {
        double x = random.nextInt(-2, 3) * bonus;
        int y = random(a, b, random);
        return y + x;
    }

    public void preparePlant(List<MatrixSample> matrixSamples, List<Double> speed, List<Double> strength, GenPlantBE plantBE, Level level) {
        BlockPos pos = plantBE.getBlockPos();
        matrixSamples.add(plantBE.sample);
        if (plantBE.spdDelta != 0) speed.add(plantBE.spdDelta);
        if (plantBE.strDelta != 0) strength.add(plantBE.strDelta);

        plantBE.sample = null;
        plantBE.setChanged();

        level.setBlock(pos, level.getBlockState(pos).setValue(GenPlantBlock.STATE, PlantState.STAGE1), 3);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.GLOW, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 20, 0.2, 0.4, 0.2, 0);
        }
    }

    public int sum(List<GeneValue> genes) {
        int purity = 0;
        for (GeneValue gene : genes) {
            purity += gene.purity();
        }
        return purity;
    }

    public int randomBonus(int x, RandomSource random) {
        return x + random.nextInt(-2, 3);
    }

    public int varToNum(List<GeneValue> geneRow, GeneValue gene) {
        for (int i = 0; i < geneRow.size(); i++) {
            if (geneRow.get(i) == gene) return i;
        }
        return 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type){
        return TickableBE.getTickerHelper(level);
    }
}