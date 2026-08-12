package net.awyvrix.genetics.content.blocks.centrifuge;

import net.minecraft.util.StringRepresentable;

public enum CentrifugeState implements StringRepresentable {
    EMPTY,
    STAGE1,
    STAGE2,
    STAGE3,
    STAGE4,
    STAGE5,
    STAGE6;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
} 