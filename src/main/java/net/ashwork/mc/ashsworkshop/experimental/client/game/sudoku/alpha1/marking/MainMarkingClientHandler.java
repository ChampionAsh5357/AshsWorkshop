package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking;

import it.unimi.dsi.fastutil.ints.Int2CharArrayMap;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.MainMarking;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrarAlpha1;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

public class MainMarkingClientHandler implements MarkingClientHandler<MainMarking> {

    @Override
    public RenderState render(MainMarking marking, GuiGraphics guiGraphics, AbstractWidget widget, Font font, Int2CharArrayMap invalidValues, boolean locked, int border, float margin) {
        if (marking.getValue() != null) {
            String text = String.valueOf(marking.getValue());
            float textWidth = font.width(text) - 1;
            float textHeight = font.lineHeight - 2;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(widget.getX() + widget.getWidth() / 2f, widget.getY() + widget.getHeight() / 2f, 0);
            float scaleValue = (widget.getHeight() - border * 2 - margin * 2 - widget.getHeight() / 16f) / textHeight;
            guiGraphics.pose().scale(scaleValue, scaleValue, 1f);
            guiGraphics.drawString(font, text, -textWidth / 2f, -textHeight / 2f, locked ? 0xFF000000 : 0xFF526BEC, false);
            guiGraphics.pose().popPose();

            return RenderState.STOP;
        }

        return RenderState.CONTINUE;
    }

    @Override
    public boolean canModifyMarking(int modifiers) {
        return true;
    }

    @Override
    public int numOfModifiers() {
        return 0;
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MAIN;
    }

    @Override
    public SudokuMarking.Type type() {
        return MarkingRegistrarAlpha1.MAIN.value();
    }
}
