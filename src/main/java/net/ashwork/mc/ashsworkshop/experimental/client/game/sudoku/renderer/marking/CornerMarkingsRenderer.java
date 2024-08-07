package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.SudokuObjectRenderer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.CornerMarkings;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CornerMarkingsRenderer implements SudokuObjectRenderer<CornerMarkings> {

    private static final List<Function<Context, Offset>> POSITIONS = List.of(
            ctx -> new Offset(0, 0, ctx.margin, ctx.margin),
            ctx -> new Offset(ctx.width, 0, - ctx.margin - ctx.textWidth, ctx.margin),
            ctx -> new Offset(0, ctx.height, ctx.margin, - ctx.margin - ctx.textHeight),
            ctx -> new Offset(ctx.width, ctx.height, - ctx.margin - ctx.textWidth, - ctx.margin - ctx.textHeight),
            ctx -> new Offset(ctx.width / 2f, 0, - ctx.textWidth / 2f, ctx.margin),
            ctx -> new Offset(ctx.width / 2f, ctx.height, - ctx.textWidth / 2f, - ctx.margin - ctx.textHeight),
            ctx -> new Offset(0, ctx.height / 2f, ctx.margin, - ctx.textHeight / 2f),
            ctx -> new Offset(ctx.width, ctx.height / 2f, - ctx.margin - ctx.textWidth, - ctx.textHeight / 2f),
            ctx -> new Offset(ctx.width / 2f, ctx.height / 2f, - ctx.textWidth / 2f, - ctx.textHeight / 2f)
    );

    @Override
    public boolean render(GuiGraphics graphics, CornerMarkings marking, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        if (!locked && !marking.getValues().isEmpty()) {

            int count = 0;

            // We subtract 1 since the text width goes one larger than the actual text
            float scalingTextWidth = font.width("6") - 1;
            // We subtract 2 since the line height has a margin of 1 on the top and bottom of the text
            float textHeight = font.lineHeight - 2;
            float xScale = (width - margin * 2) / (scalingTextWidth * 5);
            float yScale = (height - margin * 2) / (((textHeight + 3) * 3) - 3);
            float scale = Math.min(1f, Math.max(xScale, yScale));
            for (Character value : marking.getValues()) {
                var textColor = invalidChecker.test(value) ? 0XFFDC574D : 0xFF526BEC;
                String text = String.valueOf(value);
                // We subtract 1 since the text width goes one larger than the actual text
                float textWidth = font.width(text) - 1;
                var ctx = new Context(width, height, margin, textWidth, textHeight);
                var position = POSITIONS.get(count).apply(ctx);
                graphics.pose().pushPose();
                graphics.pose().translate(x + position.translationX, y + position.translationY, 0f);
                graphics.pose().scale(scale, scale, 1f);
                graphics.drawString(font, text, position.textX, position.textY, textColor, false);
                graphics.pose().popPose();
                count++;
            }
        }

        return false;
    }

    private record Context(int width, int height, float margin, float textWidth, float textHeight) {}
    private record Offset(float translationX, float translationY, float textX, float textY) {}
}
