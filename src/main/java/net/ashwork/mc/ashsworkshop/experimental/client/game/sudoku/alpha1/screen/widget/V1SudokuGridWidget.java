package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.screen.widget;

import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.SudokuGrid;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class V1SudokuGridWidget extends AbstractWidget {

    private final List<V1SudokuBoxWidget> boxes;
    private final Set<V1SudokuBoxWidget> focused;
    private final Font font;
    private final int boxLength;
    private final int border;

    public V1SudokuGridWidget(Font font, SudokuGrid grid, int centerX, int centerY, int boxLength, int border, float margin) {
        super(
                centerX - (boxLength * 9 + border * 10) / 2, centerY - (boxLength * 9 + border * 10) / 2,
                boxLength * 9 + border * 10, boxLength * 9 + border * 10, Component.empty()
        );
        this.boxes = new ArrayList<>();
        this.focused = new HashSet<>();
        this.font = font;
        this.boxLength = boxLength;
        this.border = border;

        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                // Initial offset + box length * index + offset for box
                var box = new V1SudokuBoxWidget(
                        font,
                        grid.get(i, j),
                        border + (boxLength + border) * i,
                        border + (boxLength + border) * j,
                        i, j,
                        boxLength, 1, margin
                );

                boxes.add(box);
            }
        }

        this.boxes.forEach(box -> {
            if (box.getValue() != null) {
                // Check indexes
                for (int idx = 0; idx < 9; idx++) {
                    this.setInvalidBox(box, box.getYIdx() * 9 + idx, box.getValue());
                    this.setInvalidBox(box, idx * 9 + box.getXIdx(), box.getValue());
                    this.setInvalidBox(box, (this.getBoxBorderStart(box.getYIdx()) + (idx / 3)) * 9 + this.getBoxBorderStart(box.getXIdx()) + (idx % 3), box.getValue());
                }
            }
        });
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.drawString(this.font, "A1", 0, 0, 0xFFFFFFFF);
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFBBBBBB);
        for (int idx = 0; idx < 4; idx++) {
            guiGraphics.fill(this.getX(), this.getY() + (this.boxLength + this.border) * idx * 3, this.getX() + this.getWidth(), this.getY() + (this.boxLength + this.border) * idx * 3 + 1, 0xFF000000);
            guiGraphics.fill(this.getX() + (this.boxLength + this.border) * idx * 3, this.getY(), this.getX() + (this.boxLength + this.border) * idx * 3 + 1, this.getY() + this.getHeight(), 0xFF000000);
        }
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 10);
        this.boxes.forEach(box -> box.render(guiGraphics, mouseX, mouseY, partialTick));
        guiGraphics.pose().popPose();
    }

    @Override
    public void playDownSound(SoundManager handler) {

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        double xClick = mouseX - this.getX();
        double yClick = mouseY - this.getY();

        // Get clicked box
        var selectedBox = this.boxes.stream().filter(
                box -> xClick >= box.getX() && xClick < box.getX() + box.getWidth()
                && yClick >= box.getY() && yClick < box.getY() + box.getHeight()
        ).findFirst();

        this.focused.forEach(V1SudokuBoxWidget::unselect);
        this.focused.clear();
        selectedBox.ifPresent(box -> {
            box.select();
            this.focused.add(box);
        });
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        double xClick = mouseX - this.getX();
        double yClick = mouseY - this.getY();

        // Get all clicked boxes
        this.boxes.stream().filter(
                box -> xClick >= box.getX() && xClick < box.getX() + box.getWidth()
                        && yClick >= box.getY() && yClick < box.getY() + box.getHeight()
        ).forEach(box -> {
            if (this.focused.add(box)) {
                box.select();
            }
        });
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.updateValue(box -> box.keyPressed(keyCode, scanCode, modifiers)) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean updateValue(Consumer<V1SudokuBoxWidget> run) {
        if (!this.focused.isEmpty()) {
            this.focused.forEach(box -> {
                var oldValue = box.getValue();
                run.accept(box);
                var newValue = box.getValue();
                if (!Objects.equals(oldValue, newValue)) {
                    // Values Changed

                    // Check indexes
                    for (int idx = 0; idx < 9; idx++) {
                        this.setInvalidBox(box, box.getYIdx() * 9 + idx, newValue);
                        this.setInvalidBox(box, idx * 9 + box.getXIdx(), newValue);
                        this.setInvalidBox(box, (this.getBoxBorderStart(box.getYIdx()) + (idx / 3)) * 9 + this.getBoxBorderStart(box.getXIdx()) + (idx % 3), newValue);
                    }
                }
            });
            return true;
        }
        return false;
    }

    private int getBoxBorderStart(int number) {
        if (number >= 6) return 6;
        if (number >= 3) return 3;
        return 0;
    }

    private void setInvalidBox(V1SudokuBoxWidget box, int index, Character newValue) {
        var invalidBox = this.boxes.get(index);
        if (invalidBox != box) {
            if (newValue != null) {
                invalidBox.addInvalidValue(box.getIndex(), newValue);
            } else {
                invalidBox.removeInvalidValue(box.getIndex());
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
