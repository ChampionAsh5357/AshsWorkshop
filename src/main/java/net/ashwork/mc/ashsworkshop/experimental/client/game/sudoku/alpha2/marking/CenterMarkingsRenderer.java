package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.marking;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.RenderingCache;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.CenterMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.function.Predicate;

public class CenterMarkingsRenderer implements MarkingRenderer<Character, CenterMarkings> {

    @Override
    public boolean render(GuiGraphics graphics, CenterMarkings marking, RenderingCache cache, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        if (!locked && !marking.getValues().isEmpty()) {
            graphics.pose().pushPose();
            MutableComponent centerComponent = Component.empty();
            for (Character value : marking.getValues()) {
                centerComponent.append(Component.literal(String.valueOf(value)).setStyle(Style.EMPTY.withColor(
                        invalidChecker.test(value) ? INVALID_MARKING_COLOR : MARKING_COLOR
                )));
            }
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

    @Override
    public SudokuBoxLayer layer() {
        return SudokuBoxLayer.MARKINGS;
    }

    @Override
    public SudokuMarking.Type<Character, CenterMarkings> type() {
        return MarkingRegistrar.CENTER.get();
    }
}
