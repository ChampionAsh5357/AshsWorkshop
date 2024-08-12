/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.screen.widget;

import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.BoxConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGrid;
import net.ashwork.mc.ashsworkshop.init.ConstraintTypeRegistrar;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

// TODO: REWRITE
public class SudokuGridWidget extends AbstractWidget {

    private final SudokuGrid grid;
    private final List<SudokuBoxWidget> boxes;
    private final Set<SudokuBoxWidget> focused;
    private final Font font;
    private final int boxLength;
    private final int border;

    // TODO: Patchy thing until constraint renderers are added
    @Nullable
    private final BoxConstraint boxConstraint;

    public SudokuGridWidget(Font font, SudokuGrid grid, int centerX, int centerY, int boxLength, int border, float margin) {
        super(
                centerX - (boxLength * grid.getGridLength() + border * (grid.getGridLength() + 1)) / 2, centerY - (boxLength * grid.getGridLength() + border * (grid.getGridLength() + 1)) / 2,
                boxLength * grid.getGridLength() + border * (grid.getGridLength() + 1), boxLength * grid.getGridLength() + border * (grid.getGridLength() + 1), Component.empty()
        );
        this.grid = grid;
        this.boxes = new ArrayList<>();
        this.focused = new HashSet<>();
        this.font = font;
        this.boxLength = boxLength;
        this.border = border;

        // TODO: Remove when renderer is properly set up
        this.boxConstraint = this.grid.getSettings().value().constraints().stream()
                .map(Holder::value).filter(constr -> constr.type() == ConstraintTypeRegistrar.BOX.get()).findFirst()
                .map(BoxConstraint.class::cast).orElse(null);

        for (int j = 0; j < grid.getGridLength(); j++) {
            for (int i = 0; i < grid.getGridLength(); i++) {
                // Initial offset + box length * index + offset for box
                var box = new SudokuBoxWidget(
                        this.font,
                        grid.getBox(j, i),
                        this.border + (this.boxLength + this.border) * i,
                        this.border + (this.boxLength + this.border) * j,
                        i, j,
                        this.boxLength, 1, margin
                );

                boxes.add(box);
            }
        }

        this.boxes.forEach(box -> {
            var value = box.getValue();
            if (value != null) {
                this.grid.applyConstraints(box.getYIdx(), box.getXIdx(), (rowIdx, columnIdx) ->
                        this.setInvalidBox(box, rowIdx * this.grid.getGridLength() + columnIdx, value)
                );
            }
        });
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFBBBBBB);
        if (this.boxConstraint != null) {
            // Horizontal lines
            var horizontalSkip = this.grid.getGridLength() / this.boxConstraint.columnSize();
            for (int idx = 0; idx < this.boxConstraint.columnSize() + 1; idx++) {
                guiGraphics.fill(this.getX(), this.getY() + (this.boxLength + this.border) * idx * horizontalSkip, this.getX() + this.getWidth(), this.getY() + (this.boxLength + this.border) * idx * horizontalSkip + 1, 0xFF000000);

            }

            // Vertical lines
            var verticalSkip = this.grid.getGridLength() / this.boxConstraint.rowSize();
            for (int idx = 0; idx < this.boxConstraint.rowSize() + 1; idx++) {
                guiGraphics.fill(this.getX() + (this.boxLength + this.border) * idx * verticalSkip, this.getY(), this.getX() + (this.boxLength + this.border) * idx * verticalSkip + 1, this.getY() + this.getHeight(), 0xFF000000);
            }
        }
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX(), this.getY(), 10);
        this.boxes.forEach(box -> box.render(guiGraphics, mouseX, mouseY, partialTick));
        guiGraphics.pose().popPose();

        this.grid.getSettings().value().attribution().ifPresent(info -> {
            guiGraphics.fill(
                    this.getX() + this.getWidth() + 2, this.getY() + this.getHeight() / 2 - 30,
                    this.getX() + this.getWidth() + 82, this.getY() + this.getHeight() / 2 + 30,
                    0xFF658764
            );
            guiGraphics.fill(
                    this.getX() + this.getWidth() + 2, this.getY() + this.getHeight() / 2 - 30,
                    this.getX() + this.getWidth() + 82, this.getY() + this.getHeight() / 2 - 15,
                    0xFF691512
            );
            var titleSizeX = this.font.width(info.title());
            var titleSizeY = this.font.lineHeight - 1;
            guiGraphics.drawString(
                    this.font, info.title(),
                    this.getX() + this.getWidth() + 2 + (80 - titleSizeX) / 2, this.getY() + this.getHeight() / 2 - 30 + (15 - titleSizeY) / 2,
                    0xFFFFFFFF,
                    false
            );

            // TODO: Handle text wrapping and scissor
            info.description().ifPresent(description -> {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(this.getX() + this.getWidth() + 2 + 2, this.getY() + (float) this.getHeight() / 2 - 15 + 2, 0f);
                guiGraphics.pose().scale(0.5f, 0.5f, 1f);
                guiGraphics.drawString(
                        this.font, description,
                        0, 0,
                        0xFFFFFFFF,
                        false
                );
                guiGraphics.pose().popPose();
            });
        });
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

        if (!Screen.hasShiftDown()) {
            this.focused.forEach(SudokuBoxWidget::unselect);
            this.focused.clear();
        }
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

    private boolean updateValue(Consumer<SudokuBoxWidget> run) {
        if (!this.focused.isEmpty()) {
            this.focused.forEach(box -> {
                var oldValue = box.getValue();
                run.accept(box);
                var newValue = box.getValue();
                if (!Objects.equals(oldValue, newValue)) {
                    // Values Changed
                    this.grid.applyConstraints(box.getYIdx(), box.getXIdx(), (rowIdx, columnIdx) ->
                            this.setInvalidBox(box, rowIdx * this.grid.getGridLength() + columnIdx, newValue)
                    );
                }
            });
            return true;
        }
        return false;
    }

    private void setInvalidBox(SudokuBoxWidget box, int index, Character newValue) {
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
