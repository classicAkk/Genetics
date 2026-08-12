package net.awyvrix.genetics.content.data;

import net.awyvrix.genetics.Genetics;
import net.awyvrix.genetics.content.samples.BloodSample;
import net.awyvrix.genetics.content.samples.CellSample;
import net.awyvrix.genetics.content.samples.MatrixSample;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Genetics.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BloodSample>> BLOOD_SAMPLE =
            COMPONENTS.register("blood_sample", () -> DataComponentType.<BloodSample>builder().persistent(BloodSample.CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CellSample>> CELL_SAMPLE =
            COMPONENTS.register("cell_sample", () -> DataComponentType.<CellSample>builder().persistent(CellSample.CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MatrixSample>> MATRIX_SAMPLE =
            COMPONENTS.register("matrix_sample", () -> DataComponentType.<MatrixSample>builder().persistent(MatrixSample.CODEC).build());
}