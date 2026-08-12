package net.awyvrix.genetics.content.blocks.centrifuge;

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

public class CentrifugeBE extends BlockEntity {
    public MatrixSample matrix = new MatrixSample(new ArrayList<>());

    public CentrifugeBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CENTRIFUGE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        MatrixSample.CODEC.encodeStart(NbtOps.INSTANCE, matrix)
                .resultOrPartial(error -> Genetics.LOGGER.error("Failed to save MatrixSample: {}", error))
                .ifPresent(nbt -> tag.put("Matrix", nbt));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("Matrix")) {
            MatrixSample.CODEC.parse(NbtOps.INSTANCE, tag.get("Matrix"))
                    .resultOrPartial(error -> Genetics.LOGGER.error("Failed to load MatrixSample: {}", error))
                    .ifPresent(matrix -> this.matrix = matrix);
        }
    }
}