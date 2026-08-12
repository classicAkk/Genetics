package net.awyvrix.genetics.content.samples;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.awyvrix.genetics.content.data.custom.GeneValue;

import java.util.ArrayList;
import java.util.List;

public record MatrixSample(List<GeneValue> genes) {
    public static final Codec<MatrixSample> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    GeneValue.CODEC.listOf()
                            .validate(list ->
                                    list.size() <= 6
                                            ? DataResult.success(list)
                                            : DataResult.error(
                                            () -> "MatrixSample cannot contain more than 6 genes"
                                    )
                            )
                            .fieldOf("genes")
                            .forGetter(MatrixSample::genes)
            ).apply(instance, MatrixSample::new));

    public static MatrixSample DEFAULT() {
        return new MatrixSample(new ArrayList<>());
    }
}