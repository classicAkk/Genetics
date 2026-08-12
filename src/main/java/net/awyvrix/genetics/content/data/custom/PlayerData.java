package net.awyvrix.genetics.content.data.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;

public record PlayerData(Map<GeneType, Integer> dna, Map<GeneType, Boolean> applied) {
    public static final Codec<PlayerData> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.unboundedMap(GeneType.CODEC, Codec.INT).fieldOf("dna").forGetter(PlayerData::dna),
                            Codec.unboundedMap(GeneType.CODEC, Codec.BOOL).fieldOf("applied").forGetter(PlayerData::applied)
                    ).apply(instance, PlayerData::new)
            );
    public PlayerData {
        dna = new HashMap<>(dna);
    }
}