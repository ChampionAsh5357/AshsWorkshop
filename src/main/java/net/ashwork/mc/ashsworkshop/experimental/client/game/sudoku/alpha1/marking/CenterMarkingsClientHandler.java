package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking;

import it.unimi.dsi.fastutil.ints.Int2CharArrayMap;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.CenterMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrarAlpha1;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.lwjgl.glfw.GLFW;

public class CenterMarkingsClientHandler implements MarkingClientHandler<CenterMarkings> {

    @Override
    public RenderState render(CenterMarkings marking, GuiGraphics graphics, AbstractWidget widget, Font font, Int2CharArrayMap invalidValues, boolean locked, int border, float margin) {
        if (!locked && !marking.getValues().isEmpty()) {
            graphics.pose().pushPose();
            MutableComponent centerComponent = Component.empty();
            for (Character value : marking.getValues()) {
                centerComponent.append(Component.literal(String.valueOf(value)).setStyle(Style.EMPTY.withColor(
                        invalidValues.containsValue(value.charValue()) ? 0XFFDC574D : 0xFF526BEC
                )));
            }
            FormattedCharSequence centerText = centerComponent.getVisualOrderText();
            float textWidth = font.width(centerText) - 1;
            float textHeight = font.lineHeight - 2;
            graphics.pose().translate(widget.getX() + widget.getWidth() / 2f, widget.getY() + widget.getHeight() / 2f, 0f);
            float centerScalingFactor = widget.getHeight() / (4f * textHeight);
            if (textWidth >= widget.getWidth()) {
                centerScalingFactor = Math.min((widget.getWidth() - margin) / textWidth, centerScalingFactor);
            }
            graphics.pose().scale(centerScalingFactor, centerScalingFactor, 1f);
            graphics.drawString(font, centerText, -textWidth / 2f, -textHeight / 2f, 0xFFFFFFFF, false);
            graphics.pose().popPose();
        }

        return RenderState.CONTINUE;
    }

    @Override
    public boolean canModifyMarking(int modifiers) {
        return (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
    }

    @Override
    public int numOfModifiers() {
        return 1;
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MARKINGS;
    }

    @Override
    public SudokuMarking.Type type() {
        return MarkingRegistrarAlpha1.CENTERS.value();
    }
}
