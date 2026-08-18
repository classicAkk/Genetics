package net.awyvrix.genetics.registry;

import net.awyvrix.genetics.content.data.custom.GeneType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public final class GenesRegistry {
    private static final Map<EntityType<?>, GeneType> GENES = new HashMap<>();

    static {
        GENES.put(EntityType.AREA_EFFECT_CLOUD, GeneType.FILLED);
        GENES.put(EntityType.RABBIT, GeneType.JUMP_BOOST);
        GENES.put(EntityType.BLAZE, GeneType.FIRE_RESISTANCE);
        GENES.put(EntityType.IRON_GOLEM, GeneType.STRENGTH);
        GENES.put(EntityType.ARMADILLO, GeneType.RESISTANCE);
        GENES.put(EntityType.AXOLOTL, GeneType.REGENERATION);
        GENES.put(EntityType.PIG, GeneType.SATURATION);
        GENES.put(EntityType.BAT, GeneType.NIGHT_VISION);
        GENES.put(EntityType.DOLPHIN, GeneType.SPEED);
        GENES.put(EntityType.CREEPER, GeneType.EXPLOSION_RESISTANCE);
        GENES.put(EntityType.GUARDIAN, GeneType.WATER_SPEED);
        GENES.put(EntityType.SALMON, GeneType.WATER_BREATHING);
        GENES.put(EntityType.ENDER_DRAGON, GeneType.DRAGON);
        GENES.put(EntityType.WITHER, GeneType.WITHER);
        GENES.put(EntityType.CAT, GeneType.CAT);
        GENES.put(EntityType.FOX, GeneType.FOX);
    }

    public static GeneType get(LivingEntity entity) {
        return GENES.get(entity.getType());
    }
}