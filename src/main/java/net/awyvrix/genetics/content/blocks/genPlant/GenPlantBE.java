package net.awyvrix.genetics.content.blocks.genPlant;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.inits.ModBlockEntities;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GenPlantBE extends BlockEntity {
    public MatrixSample sample;

    public GenPlantBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GEN_PLANT.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (sample != null) {
            MatrixSample.CODEC.encodeStart(NbtOps.INSTANCE, sample)
                    .resultOrPartial(error -> Genetics.LOGGER.error("Failed to save MatrixSample: {}", error))
                    .ifPresent(nbt -> tag.put("Sample", nbt));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("Sample")) {
            MatrixSample.CODEC.parse(NbtOps.INSTANCE, tag.get("Sample"))
                    .resultOrPartial(error -> Genetics.LOGGER.error("Failed to load MatrixSample: {}", error))
                    .ifPresent(sample -> this.sample = sample);
        }
    }
}