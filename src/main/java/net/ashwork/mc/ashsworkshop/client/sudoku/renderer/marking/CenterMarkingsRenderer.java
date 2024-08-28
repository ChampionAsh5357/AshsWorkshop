/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.renderer.marking;

import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.SudokuObjectRenderer;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.CenterMarkings;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.function.Predicate;

/**
 * Renders the markings to display in the center of the box.
 */
public class CenterMarkingsRenderer implements SudokuObjectRenderer<CenterMarkings> {

    @Override
    public boolean render(GuiGraphics graphics, CenterMarkings marking, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        // Should only render for unlocked boxes where the marking isn't empty
        if (!locked && !marking.getValues().isEmpty()) {
            graphics.pose().pushPose();

            // Construct the component to display
            MutableComponent centerComponent = Component.empty();
            for (Character value : marking.getValues()) {
                centerComponent.append(Component.literal(String.valueOf(value)).setStyle(Style.EMPTY.withColor(
                        invalidChecker.test(value) ? INVALID_MARKING_COLOR : MARKING_COLOR
                )));
            }

            // Handle rendering and sizing
            FormattedCharSequence centerText = centerComponent.getVisualOrderText();
            float textWidth = font.width(centerText) - 1;
            float textHeight = font.lineHeight - 2;
            graphics.pose().translate(x + width / 2f, y + height / 2f, 0f);
            float centerScalingFactor = height / (4f * textHeight);
            if (textWidth >= width) {
                centerScalingFactor = Math.min((width - margin) / textWidth, centerScalingFactor);
            }
            graphics.pose().scale(centerScalingFactor, centerScalingFactor, 1f);
            graphics.drawString(font, centerText, -textWidth / 2f, -textHeight / 2f, 0xFFFFFFFF, false);
            graphics.pose().popPose();
        }

        return false;
    }
}
