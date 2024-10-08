/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.constraint;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.init.ConstraintTypeRegistrar;

import java.util.function.BiConsumer;

public class RowConstraint implements SudokuConstraint {

    public static final RowConstraint INSTANCE = new RowConstraint();
    public static final MapCodec<RowConstraint> CODEC = MapCodec.unit(INSTANCE);

    private RowConstraint() {}

    @Override
    public void apply(SudokuGridSettings settings, int rowIdx, int columnIdx, BiConsumer<Integer, Integer> constraint) {
        for (int idx = 0; idx < settings.gridLength(); idx++) {
            constraint.accept(idx, columnIdx);
        }
    }

    @Override
    public Type<?> type() {
        return ConstraintTypeRegistrar.ROW.get();
    }
}
