package net.awyvrix.genetics.content.blocks.genPlant;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.blocks.petriDish.PetriBlock;
import net.awyvrix.genetics.content.blocks.petriDish.PetriState;
import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.content.data.custom.GeneValue;
import net.awyvrix.genetics.content.inits.ModBlockEntities;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.awyvrix.genetics.util.TickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GenPlantBE extends BlockEntity implements TickableBE {
    public MatrixSample gene;

    public MatrixSample sample;
    public MatrixSample target;

    boolean dirty = true;
    double growTimer;

    double spdDelta;
    double strDelta;

    double strPurity;
    double spdPurity;

    public GenPlantBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GEN_PLANT.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (gene != null) {
            MatrixSample.CODEC.encodeStart(NbtOps.INSTANCE, gene)
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to save Gene: {}", error))
                    .ifPresent(nbt -> tag.put("Gene", nbt));
        }

        if (sample != null) {
            MatrixSample.CODEC.encodeStart(NbtOps.INSTANCE, sample)
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to save Sample: {}", error))
                    .ifPresent(nbt -> tag.put("Sample", nbt));
        }

        if (target != null) {
            MatrixSample.CODEC.encodeStart(NbtOps.INSTANCE, target)
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to save Target: {}", error))
                    .ifPresent(nbt -> tag.put("Target", nbt));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("Gene")) {
            MatrixSample.CODEC.parse(NbtOps.INSTANCE, tag.get("Gene"))
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to load Gene: {}", error))
                    .ifPresent(value -> this.gene = value);
        }

        if (tag.contains("Sample")) {
            MatrixSample.CODEC.parse(NbtOps.INSTANCE, tag.get("Sample"))
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to load Sample: {}", error))
                    .ifPresent(value -> this.sample = value);
        }

        if (tag.contains("Target")) {
            MatrixSample.CODEC.parse(NbtOps.INSTANCE, tag.get("Target"))
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to load Target: {}", error))
                    .ifPresent(value -> this.target = value);
        }
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) return;
        if (dirty && gene != null) {
            for (GeneValue gene : gene.genes()) {
                if (gene.type() == GeneType.SPEED) spdPurity += gene.purity();
                if (gene.type() == GeneType.STRENGTH) strPurity += gene.purity();
            }
            spdDelta = 1 + (spdPurity / 100) / 2;
            strDelta = 1 + (strPurity / 100) / 2;
            dirty = false;
        }
        BlockState state = level.getBlockState(getBlockPos());

        if (state.getValue(GenPlantBlock.STATE) != PlantState.STAGE0 && growTimer > 0) {
            growTimer--;
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.VAULT_CONNECTION, worldPosition.getX() + 0.5, worldPosition.getY(), worldPosition.getZ() + 0.5, 5, 0.2, 0.4, 0.2, 0);
            }

            if (growTimer == 0) {
                sample = new MatrixSample(target.genes());
                target = null;

                level.setBlock(worldPosition, state.setValue(GenPlantBlock.STATE, PlantState.LOADED), 3);
                level.playSound(null, worldPosition, SoundEvents.ALLAY_HURT, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }
    }
}