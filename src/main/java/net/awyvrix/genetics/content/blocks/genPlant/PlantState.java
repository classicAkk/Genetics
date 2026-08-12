package net.awyvrix.genetics.content.blocks.genPlant;

import net.minecraft.util.StringRepresentable;

public enum PlantState implements StringRepresentable {
    STAGE0,
    STAGE1,
    LOADED;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
} 