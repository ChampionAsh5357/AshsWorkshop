/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.renderer.marking;

import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.SudokuObjectRenderer;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.MainMarking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Predicate;

/**
 * Renders the main marking value.
 */
public class MainMarkingRenderer implements SudokuObjectRenderer<MainMarking> {

    private static final int LOCKED_VALUE = 0xFF000000;

    @Override
    public boolean render(GuiGraphics graphics, MainMarking marking, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        // Only renders if there is a main marking
        if (marking.getValue() != null) {
            String text = String.valueOf(marking.getValue());
            float textWidth = font.width(text) - 1;
            float textHeight = font.lineHeight - 2;

            // Scale according to box size
            graphics.pose().pushPose();
            graphics.pose().translate(x + width / 2f, y + height / 2f, 0);
            float scaleValue = (height - selectedBorder * 2 - margin * 2 - height / 16f) / textHeight;
            graphics.pose().scale(scaleValue, scaleValue, 1f);
            // Change color based on if the box is locked or not
            graphics.drawString(font, text, -textWidth / 2f, -textHeight / 2f, locked ? LOCKED_VALUE : MARKING_COLOR, false);
            graphics.pose().popPose();
            return true;
        }

        return false;
    }
}
