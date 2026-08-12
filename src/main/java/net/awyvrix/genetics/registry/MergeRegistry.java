package net.awyvrix.genetics.registry;


import net.awyvrix.genetics.content.data.custom.GeneType;

import java.util.*;

public final class MergeRegistry {
    private static final Map<Set<GeneType>, GeneType> MERGES = new HashMap<>();
    private static final Map<GeneType, Set<GeneType>> RECIPES = new HashMap<>();

    static {
        register(GeneType.HASTE, GeneType.SPEED, GeneType.STRENGTH);
        register(GeneType.JUMP_BOOST, GeneType.SPEED, GeneType.SLOW_FALLING);
        register(GeneType.FLY, GeneType.SLOW_FALLING, GeneType.SPEED, GeneType.WITHER, GeneType.DRAGON);
        register(GeneType.REGENERATION, GeneType.SATURATION, GeneType.RESISTANCE);
        register(GeneType.ABSORPTION, GeneType.STRENGTH, GeneType.RESISTANCE);
        register(GeneType.CONDUIT_POWER, GeneType.WATER_BREATHING, GeneType.WATER_SPEED);
        register(GeneType.DOLPHINS_GRACE, GeneType.WATER_SPEED, GeneType.SPEED);
        register(GeneType.RESISTANCE, GeneType.STRENGTH, GeneType.FIRE_RESISTANCE);
        register(GeneType.WITHER_RESISTANCE, GeneType.WITHER, GeneType.RESISTANCE);
        register(GeneType.ANTHRO, GeneType.CAT, GeneType.FOX, GeneType.ASTRACHID);
    }

    public static GeneType get(GeneType... genes) {
        return MERGES.get(Set.of(genes));
    }

    public static GeneType get(Set<GeneType> key) {
        return MERGES.get(key);
    }

    public static Set<GeneType> getRecipes(GeneType key) {
        return RECIPES.get(key);
    }

    private static void register(GeneType result, GeneType... ingredients) {
        MERGES.put(Set.of(ingredients), result);
        RECIPES.put(result, Set.of(ingredients));
    }
}