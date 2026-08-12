package net.awyvrix.genetics.registry;

import net.awyvrix.genetics.geneEffects.WaterBreathing;
import net.awyvrix.genetics.content.data.custom.GeneType;
import net.awyvrix.genetics.geneEffects.*;
import net.awyvrix.genetics.geneEffects.interfaces.GeneEffect;

import java.util.*;

public final class EffectRegistry {
    private static final Map<GeneType, GeneEffect> GENES = new HashMap<>();
    public static final List<UUID> anthros = new ArrayList<>();
    public static final List<UUID> witherImmune = new ArrayList<>();
    public static final List<UUID> wither = new ArrayList<>();
    public static final List<UUID> dragon = new ArrayList<>();
    public static final List<UUID> explosionImmune = new ArrayList<>();

    static {
        GENES.put(GeneType.SPEED, new Speed());
        GENES.put(GeneType.STRENGTH, new Strength());
        GENES.put(GeneType.REGENERATION, new Regeneration());
        GENES.put(GeneType.FIRE_RESISTANCE, new FireResistance());
        GENES.put(GeneType.NIGHT_VISION, new NightVision());
        GENES.put(GeneType.HASTE, new Haste());
        GENES.put(GeneType.RESISTANCE, new Resistance());
        GENES.put(GeneType.SATURATION, new Saturation());
        GENES.put(GeneType.WATER_BREATHING, new WaterBreathing());
        GENES.put(GeneType.WITHER_RESISTANCE, new WitherResistance());
        GENES.put(GeneType.EXPLOSION_RESISTANCE, new ExplosionResistance());
        GENES.put(GeneType.WATER_SPEED, new WaterSpeed());
        GENES.put(GeneType.JUMP_BOOST, new JumpBoost());
        GENES.put(GeneType.FLY, new Fly());
        GENES.put(GeneType.SLOW_FALLING, new SlowFalling());
        GENES.put(GeneType.ABSORPTION, new Absorption());
        GENES.put(GeneType.CONDUIT_POWER, new ConduitPower());
        GENES.put(GeneType.DOLPHINS_GRACE, new DolphinGrace());
        GENES.put(GeneType.CAT, new Cat());
        GENES.put(GeneType.FOX, new Fox());
        GENES.put(GeneType.ASTRACHID, new Astrachid());
        GENES.put(GeneType.ANTHRO, new Anthro());
        GENES.put(GeneType.DRAGON, new Dragon());
        GENES.put(GeneType.WITHER, new Wither());
    }

    public static GeneEffect get(GeneType type) {
        return GENES.get(type);
    }
}
