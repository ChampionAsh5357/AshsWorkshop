package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.selection;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.SudokuObjectRenderer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.MainMarking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SelectionRenderer implements SudokuObjectRenderer<Boolean> {

    private static final int SELECTION_COLOR = 0xFF69C8EC;

    @Override
    public boolean render(GuiGraphics graphics, Boolean selected, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        if (selected) {
            // Top Left -> Bottom Left
            graphics.fill(x, y, x + selectedBorder, y + height, SELECTION_COLOR);
            // Top Left -> Top Right
            graphics.fill(x, y, x + width, y + selectedBorder, SELECTION_COLOR);
            // Top Right -> Bottom Right
            graphics.fill(x + width - selectedBorder, y, x + width, y + height, SELECTION_COLOR);
            // Bottom Left -> Bottom Right
            graphics.fill(x, y + height - selectedBorder, x + width, y + height, SELECTION_COLOR);
        }
        return false;
    }
}
