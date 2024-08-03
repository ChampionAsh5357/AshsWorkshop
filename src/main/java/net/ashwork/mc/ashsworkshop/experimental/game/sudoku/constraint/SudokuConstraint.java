package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;

import java.util.function.BiConsumer;

public interface SudokuConstraint {

    Codec<SudokuConstraint> DIRECT_CODEC = ExperimentalWorkshopRegistries.SUDOKU_CONSTRAINT_TYPE
            .byNameCodec().dispatch(SudokuConstraint::type, Type::codec);
    Codec<Holder<SudokuConstraint>> REGISTRY_CODEC = RegistryFileCodec.create(ExperimentalWorkshopRegistries.SUDOKU_CONSTRAINT_KEY, DIRECT_CODEC);
    Codec<HolderSet<SudokuConstraint>> LIST_CODEC = RegistryCodecs.homogeneousList(
            ExperimentalWorkshopRegistries.SUDOKU_CONSTRAINT_KEY, DIRECT_CODEC
    );

    void apply(SudokuGridSettings settings, int rowIdx, int columnIdx, BiConsumer<Integer, Integer> constraint);

    default boolean validate(SudokuGridSettings settings) {
        return true;
    }

    SudokuConstraint.Type<?> type();

    record Type<T extends SudokuConstraint>(MapCodec<T> codec) {}
}
