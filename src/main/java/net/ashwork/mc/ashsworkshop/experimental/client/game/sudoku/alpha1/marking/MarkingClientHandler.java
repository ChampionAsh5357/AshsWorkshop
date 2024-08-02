package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking;

import it.unimi.dsi.fastutil.ints.Int2CharArrayMap;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.SudokuMarking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

public interface MarkingClientHandler<T extends SudokuMarking> {

    RenderState render(T marking, GuiGraphics graphics, AbstractWidget widget, Font font, Int2CharArrayMap invalidValues, boolean locked, int border, float margin);

    boolean canModifyMarking(int modifiers);

    int numOfModifiers();

    RenderLayer layer();

    SudokuMarking.Type type();

    enum RenderState {
        CONTINUE,
        STOP
    }

    enum RenderLayer {
        BACKGROUND,
        MAIN,
        MARKINGS
    }
}
