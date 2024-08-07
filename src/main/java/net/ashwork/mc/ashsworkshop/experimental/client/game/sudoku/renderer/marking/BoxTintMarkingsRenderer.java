package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.SudokuObjectRenderer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.BoxTintMarkings;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BoxTintMarkingsRenderer implements SudokuObjectRenderer<BoxTintMarkings> {

    private final int backgroundColor;
    
    public BoxTintMarkingsRenderer(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    
    @Override
    public boolean render(GuiGraphics graphics, BoxTintMarkings object, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        graphics.fill(x, y, x + width, y + height, this.backgroundColor);
        return false;
    }
}
