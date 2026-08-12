package net.awyvrix.genetics.content.samples;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CellSample(
    String id,
    String owner
) {
    public static final Codec<CellSample> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("uuid").forGetter(CellSample::id),
                    Codec.STRING.fieldOf("owner").forGetter(CellSample::owner)
            ).apply(instance, CellSample::new)
    );
}