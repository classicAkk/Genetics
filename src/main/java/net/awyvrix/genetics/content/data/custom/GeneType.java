package net.awyvrix.genetics.content.data.custom;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum GeneType {
    FILLED,
    SPEED,
    STRENGTH,
    REGENERATION,
    FIRE_RESISTANCE,
    NIGHT_VISION,
    HASTE,
    RESISTANCE,
    SATURATION,
    WATER_BREATHING,
    WITHER_RESISTANCE,
    EXPLOSION_RESISTANCE,
    WATER_SPEED,
    JUMP_BOOST,
    FLY,
    SLOW_FALLING,
    ABSORPTION,
    CONDUIT_POWER,
    DOLPHINS_GRACE,
    CAT,
    FOX,
    ASTRACHID,
    ANTHRO,
    DRAGON,
    WITHER;

    public String getSerializedName() {
        return Arrays.stream(this.name().split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public String getLowerCaseName() {
        return this.name().toLowerCase();
    }

    public static final Codec<GeneType> CODEC = Codec.STRING.xmap(GeneType::valueOf, GeneType::name);
}