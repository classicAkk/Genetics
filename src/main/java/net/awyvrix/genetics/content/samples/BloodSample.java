package net.awyvrix.genetics.content.samples;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.awyvrix.genetics.content.data.custom.GeneType;

public record BloodSample(
    GeneType gene,
    int purity
) {
    public static final Codec<BloodSample> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    GeneType.CODEC.fieldOf("gene").forGetter(BloodSample::gene),
                    Codec.INT.fieldOf("purity").forGetter(BloodSample::purity)
            ).apply(instance, BloodSample::new)
    );
}