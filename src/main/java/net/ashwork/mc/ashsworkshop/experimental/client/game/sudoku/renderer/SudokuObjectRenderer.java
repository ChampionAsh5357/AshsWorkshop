package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.SudokuBox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface SudokuObjectRenderer<O> {

    int MARKING_COLOR = 0xFF526BEC;
    int INVALID_MARKING_COLOR = 0XFFDC574D;

    // Returns true if rendering of higher layers should be stopped
    boolean render(GuiGraphics graphics, O object, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked);

    record Type<T, V>(T valueType, BiFunction<SudokuBox, T, V> valueGetter, SudokuBoxLayer layer) {

        public V get(SudokuBox box) {
            return this.valueGetter.apply(box, this.valueType);
        }
    }
}
