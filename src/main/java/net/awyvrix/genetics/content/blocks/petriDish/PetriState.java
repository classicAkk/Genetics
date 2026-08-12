package net.awyvrix.genetics.content.blocks.petriDish;

import net.minecraft.util.StringRepresentable;

public enum PetriState implements StringRepresentable {
    EMPTY,
    STAGE1,
    STAGE2,
    READY,
    CELLS,
    GENETIC;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
} 