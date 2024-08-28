/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.renderer.selection;

import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.SudokuObjectRenderer;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.MainMarking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Predicate;

/**
 * Renders a square when the main marking is invalid.
 */
public class InvalidMarkingRenderer implements SudokuObjectRenderer<MainMarking> {

    @Override
    public boolean render(GuiGraphics graphics, MainMarking marking, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        if (marking.getValue() != null && invalidChecker.test(marking.getValue())) {
            graphics.fill(x, y, x + width, y + height, INVALID_BACKGROUND_COLOR);
        }
        return false;
    }
}
