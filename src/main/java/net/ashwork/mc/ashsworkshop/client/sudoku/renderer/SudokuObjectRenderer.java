/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.renderer;

import net.ashwork.mc.ashsworkshop.client.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.client.sudoku.screen.widget.SudokuBoxWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * A generalized interface for rendering a sudoku object on a grid.
 *
 * @param <O> the type of the object to render
 */
public interface SudokuObjectRenderer<O> {

    int MARKING_COLOR = 0xFF526BEC;
    int INVALID_COLOR = 0XDC574D;
    int INVALID_MARKING_COLOR = 0XFF000000 | INVALID_COLOR;
    int INVALID_BACKGROUND_COLOR = 0X7F000000 | INVALID_COLOR;

    // Returns true if rendering of higher layers should be stopped
    boolean render(GuiGraphics graphics, O object, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked);

    record Type<T, V>(T valueType, BiFunction<SudokuBoxWidget, T, V> valueGetter, SudokuBoxLayer layer) {

        public V get(SudokuBoxWidget box) {
            return this.valueGetter.apply(box, this.valueType);
        }
    }
}
