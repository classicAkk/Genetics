package net.awyvrix.genetics.content.blocks.cloner;

import net.minecraft.util.StringRepresentable;

public enum ClonerState implements StringRepresentable {
    EMPTY,
    STAGE1,
    STAGE2;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
} 