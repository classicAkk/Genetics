package net.awyvrix.genetics.content.blocks.cloner;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.inits.ModBlockEntities;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class ClonerBE extends BlockEntity {
    public MatrixSample sample = new MatrixSample(new ArrayList<>());
    public MatrixSample matrix = new MatrixSample(new ArrayList<>());

    public ClonerBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLONER.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (sample != null) {
            MatrixSample.CODEC.encodeStart(NbtOps.INSTANCE, sample)
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to save Sample: {}", error))
                    .ifPresent(nbt -> tag.put("Sample", nbt));
        }
        if (matrix != null) {
            MatrixSample.CODEC.encodeStart(NbtOps.INSTANCE, matrix)
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to save Matrix: {}", error))
                    .ifPresent(nbt -> tag.put("Matrix", nbt));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("Sample")) {
            MatrixSample.CODEC.parse(NbtOps.INSTANCE, tag.get("Sample"))
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to load Sample: {}", error))
                    .ifPresent(value -> this.sample = value);
        }
        if (tag.contains("Matrix")) {
            MatrixSample.CODEC.parse(NbtOps.INSTANCE, tag.get("Matrix"))
                    .resultOrPartial(error ->
                            Genetics.LOGGER.error("Failed to load Matrix: {}", error))
                    .ifPresent(value -> this.matrix = value);
        }
    }
}