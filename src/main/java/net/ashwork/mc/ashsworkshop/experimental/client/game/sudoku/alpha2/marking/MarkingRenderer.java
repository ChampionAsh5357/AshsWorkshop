package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.marking;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.RenderingCache;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.SudokuMarking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Predicate;

public interface MarkingRenderer<T, M extends SudokuMarking<T>> {

    int MARKING_COLOR = 0xFF526BEC;
    int INVALID_MARKING_COLOR = 0XFFDC574D;

    // Returns true if rendering of higher layers should be stopped
    boolean render(GuiGraphics graphics, M marking, RenderingCache cache, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked);

    SudokuBoxLayer layer();

    SudokuMarking.Type<T, M> type();
}
