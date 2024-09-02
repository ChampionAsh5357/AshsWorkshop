/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.constraint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;

import java.util.function.BiConsumer;

/**
 * A constraint that can be applied to a sudoku grid.
 */
public interface SudokuConstraint {

    Codec<SudokuConstraint> DIRECT_CODEC = WorkshopRegistries.SUDOKU_CONSTRAINT_TYPE
            .byNameCodec().dispatch(SudokuConstraint::type, Type::codec);
    Codec<Holder<SudokuConstraint>> REGISTRY_CODEC = RegistryFileCodec.create(WorkshopRegistries.SUDOKU_CONSTRAINT_KEY, DIRECT_CODEC);
    Codec<HolderSet<SudokuConstraint>> LIST_CODEC = RegistryCodecs.homogeneousList(
            WorkshopRegistries.SUDOKU_CONSTRAINT_KEY, DIRECT_CODEC
    );

    /**
     * Applies the constraint to the sudoku grid.
     *
     * @param settings the settings of the grid
     * @param rowIdx the index of the row the constraint is being applied to
     * @param columnIdx the index of the column the constraint is being applied to
     * @param constraint a (row, column) consumer that accepts the indexes that this position conflicts with
     */
    void apply(SudokuGridSettings settings, int rowIdx, int columnIdx, BiConsumer<Integer, Integer> constraint);

    /**
     * Validates whether the constraint is legal. Is done when the puzzle is loaded.
     *
     * @param settings the settings of the grid
     * @return {@code true} if the constraint is legal, {@code false} otherwise
     */
    default boolean validate(SudokuGridSettings settings) {
        return true;
    }

    /**
     * {@return the type of the constraint}
     */
    SudokuConstraint.Type<?> type();

    record Type<T extends SudokuConstraint>(MapCodec<T> codec) {}
}
