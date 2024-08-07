package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.selection;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.SudokuObjectRenderer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.MainMarking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class InvalidMarkingRenderer implements SudokuObjectRenderer<MainMarking> {

    @Override
    public boolean render(GuiGraphics graphics, MainMarking marking, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        if (marking.getValue() != null && invalidChecker.test(marking.getValue())) {
            graphics.fill(x, y, x + width, y + height, INVALID_BACKGROUND_COLOR);
        }
        return false;
    }
}
