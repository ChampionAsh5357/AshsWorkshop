package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking;

import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2CharArrayMap;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.CornerMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrarAlpha1;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class CornerMarkingsClientHandler implements MarkingClientHandler<CornerMarkings> {

    @Override
    public RenderState render(CornerMarkings marking, GuiGraphics graphics, AbstractWidget widget, Font font, Int2CharArrayMap invalidValues, boolean locked, int border, float margin) {
        if (!locked && !marking.getValues().isEmpty()) {


            List<Function4<AbstractWidget, Float, Float, Float, Pair<Pair<Float, Float>, Pair<Float, Float>>>> positions = List.of(
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX(), (float) wdg.getY()), Pair.of(mgn, mgn)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX() + wdg.getWidth(), (float) wdg.getY()), Pair.of(-mgn - width, mgn)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX(), (float) wdg.getY() + wdg.getHeight()), Pair.of(mgn, -mgn - height)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX() + wdg.getWidth(), (float) wdg.getY() + wdg.getHeight()), Pair.of(-mgn - width, -mgn - height)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX() + wdg.getWidth() / 2f, (float) wdg.getY()), Pair.of(-width / 2f, mgn)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX() + wdg.getWidth() / 2f, (float) wdg.getY() + wdg.getHeight()), Pair.of(-width / 2f, -mgn - height)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX(), (float) wdg.getY() + wdg.getHeight() / 2f), Pair.of(mgn, -height / 2f)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX() + wdg.getWidth(), (float) wdg.getY() + wdg.getHeight() / 2f), Pair.of(-mgn - width, -height / 2f)),
                    (wdg, mgn, width, height) -> Pair.of(Pair.of((float) wdg.getX() + wdg.getWidth() / 2f, (float) wdg.getY() + wdg.getHeight() / 2f), Pair.of(-width / 2f, -height / 2f))
            );

            int count = 0;

            // We subtract 1 since the text width goes one larger than the actual text
            float scalingTextWidth = font.width("6") - 1;
            // We subtract 2 since the line height has a margin of 1 on the top and bottom of the text
            float textHeight = font.lineHeight - 2;
            float xScale = (widget.getWidth() - margin * 2) / (scalingTextWidth * 5);
            float yScale = (widget.getHeight() - margin * 2) / (((textHeight + 3) * 3) - 3);
            float scale = Math.min(1f, Math.max(xScale, yScale));
            for (Character value : marking.getValues()) {
                var textColor = invalidValues.containsValue(value.charValue()) ? 0XFFDC574D : 0xFF526BEC;
                String text = String.valueOf(value);
                // We subtract 1 since the text width goes one larger than the actual text
                float textWidth = font.width(text) - 1;
                var position = positions.get(count).apply(widget, margin, textWidth, textHeight);
                graphics.pose().pushPose();
                graphics.pose().translate(position.getFirst().getFirst(), position.getFirst().getSecond(), 0f);
                graphics.pose().scale(scale, scale, 1f);
                graphics.drawString(font, text, position.getSecond().getFirst(), position.getSecond().getSecond(), textColor, false);
                graphics.pose().popPose();
                count++;
            }
        }

        return RenderState.CONTINUE;
    }

    @Override
    public boolean canModifyMarking(int modifiers) {
        return (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
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
        return MarkingRegistrarAlpha1.CORNERS.value();
    }
}
