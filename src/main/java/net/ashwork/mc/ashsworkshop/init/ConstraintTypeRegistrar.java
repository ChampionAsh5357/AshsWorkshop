/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.BoxConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.ColumnConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.RowConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.SudokuConstraint;

import java.util.function.Supplier;

public class ConstraintTypeRegistrar {

    public static final Supplier<SudokuConstraint.Type<RowConstraint>> ROW = registerType("row", RowConstraint.CODEC);
    public static final Supplier<SudokuConstraint.Type<ColumnConstraint>> COLUMN = registerType("column", ColumnConstraint.CODEC);
    public static final Supplier<SudokuConstraint.Type<BoxConstraint>> BOX = registerType("box", BoxConstraint.CODEC);

    private static <T extends SudokuConstraint> Supplier<SudokuConstraint.Type<T>> registerType(String name, MapCodec<T> codec) {
        return WorkshopRegistrars.SUDOKU_CONSTRAINT_TYPE.register(name, () -> new SudokuConstraint.Type<>(codec));
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
