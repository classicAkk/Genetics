package net.awyvrix.genetics.content.blocks.petriDish;

import net.awyvrix.genetics.content.inits.ModBlockEntities;
import net.awyvrix.genetics.util.TickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PetriBE extends BlockEntity implements TickableBE {
    public int cells;
    public String id;
    public String owner;
    private int growTimer;

    public PetriBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PETRI.get(), pos, state);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) return;
        BlockState state = level.getBlockState(getBlockPos());

        if (state.getValue(PetriBlock.STATE) == PetriState.CELLS && cells == 0) level.setBlock(worldPosition, state.setValue(PetriBlock.STATE, PetriState.READY), 3);
        if (state.getValue(PetriBlock.STATE) == PetriState.CELLS) growTimer++;

        if (growTimer >= 1200) {
            growTimer = 0;
            cells = Math.min(cells * 2, 16);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("Cells", cells);
        tag.putString("Id", id == null ? "" : id);
        tag.putString("Owner", owner == null ? "" : owner);
        tag.putInt("GrowTimer", growTimer);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        cells = tag.getInt("Cells");
        id = tag.getString("Id");
        owner = tag.getString("Owner");
        growTimer = tag.getInt("GrowTimer");
    }
}