package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.marking;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.RenderingCache;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.MainMarking;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Predicate;

public class MainMarkingRenderer implements MarkingRenderer<Character, MainMarking> {

    private static final int LOCKED_VALUE = 0xFF000000;

    @Override
    public boolean render(GuiGraphics graphics, MainMarking marking, RenderingCache cache, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        if (marking.getValue() != null) {
            String text = String.valueOf(marking.getValue());
            float textWidth = font.width(text) - 1;
            float textHeight = font.lineHeight - 2;

            graphics.pose().pushPose();
            graphics.pose().translate(x + width / 2f, y + height / 2f, 0);
            float scaleValue = (height - selectedBorder * 2 - margin * 2 - height / 16f) / textHeight;
            graphics.pose().scale(scaleValue, scaleValue, 1f);
            graphics.drawString(font, text, -textWidth / 2f, -textHeight / 2f, locked ? LOCKED_VALUE : MARKING_COLOR, false);
            graphics.pose().popPose();
            return true;
        }

        return false;
    }

    @Override
    public SudokuBoxLayer layer() {
        return SudokuBoxLayer.MAIN_VALUE;
    }

    @Override
    public SudokuMarking.Type<Character, MainMarking> type() {
        return MarkingRegistrar.MAIN.get();
    }
}
