package net.awyvrix.genetics.content.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GeneValue(GeneType type, int purity) {
    public GeneType type() {return type;}
    public int purity() {return purity;}

    public static final Codec<GeneValue> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    GeneType.CODEC.fieldOf("type").forGetter(GeneValue::type),
                    Codec.INT.fieldOf("value").forGetter(GeneValue::purity)
            ).apply(instance, GeneValue::new));
}
