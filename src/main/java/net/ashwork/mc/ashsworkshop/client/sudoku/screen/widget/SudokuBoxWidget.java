/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.screen.widget;

import it.unimi.dsi.fastutil.ints.Int2CharArrayMap;
import net.ashwork.mc.ashsworkshop.client.AshsWorkshopClient;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.SudokuBox;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

// TODO: REWRITE
public class SudokuBoxWidget extends AbstractWidget {

    private final Font font;
    // Represents the margin within the box border
    private final float margin;
    // Border for selection
    private final int selectedBorder;

    private final SudokuBox box;

    // Easy index lookup
    private final int xIdx, yIdx;

    private boolean selected = false;

    // Holds the invalid values
    private final Int2CharArrayMap invalidValues = new Int2CharArrayMap();

    public SudokuBoxWidget(Font font, SudokuBox box, int x, int y, int xIdx, int yIdx, int length, int selectedBorder, float margin) {
        super(x, y, length, length, Component.empty());
        this.font = font;
        this.box = box;
        this.xIdx = xIdx;
        this.yIdx = yIdx;
        this.selectedBorder = selectedBorder;
        this.margin = margin;
    }

    public boolean locked() {
        return this.box.isLocked();
    }

    public float getMargin() {
        return this.margin;
    }

    public int getSelectedBorder() {
        return this.selectedBorder;
    }

    public int getIndex() {
        return this.getYIdx() * 9 + this.getXIdx();
    }

    public int getXIdx() {
        return this.xIdx;
    }

    public int getYIdx() {
        return this.yIdx;
    }

    public Character getValue() {
        return this.box.mainValue();
    }

    public void addInvalidValue(int idx, char value) {
        this.invalidValues.put(idx, value);
    }

    public void removeInvalidValue(int idx) {
        this.invalidValues.remove(idx);
    }

    public void unselect() {
        this.selected = false;
    }

    public void select() {
        this.selected = true;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public SudokuBox box() {
        return this.box;
    }

    public Font font() {
        return this.font;
    }

    public boolean containsInvalidValue(char value) {
        return this.invalidValues.containsValue(value);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        AshsWorkshopClient.instance().sudokuRendererHandler().render(guiGraphics, this);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.locked()) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
            // Remove values
            if (!AshsWorkshopClient.instance().markingInteractionHandler().applyModifiers(modifiers, this.box::clear, false)) {
                this.box.clearAll();
            }

            return true;
        }

        var codePoint = keyToPoint(keyCode);
        if (codePoint >= '1' && codePoint <= '9' && !this.box.isLocked()) {
            AshsWorkshopClient.instance().markingInteractionHandler().applyModifiers(modifiers, type -> {
                // Force boxes that already have a value set to not do any markings but the main one
                if (this.box.mainValue() != null && type != MarkingRegistrar.MAIN.get()) {
                    return true;
                }

                this.box.mark(type, codePoint);
                return true;
            }, true);

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private char keyToPoint(int keyCode) {
        return switch (keyCode) {
            case GLFW.GLFW_KEY_KP_0, GLFW.GLFW_KEY_KP_1,
                 GLFW.GLFW_KEY_KP_2, GLFW.GLFW_KEY_KP_3,
                 GLFW.GLFW_KEY_KP_4, GLFW.GLFW_KEY_KP_5,
                 GLFW.GLFW_KEY_KP_6, GLFW.GLFW_KEY_KP_7,
                 GLFW.GLFW_KEY_KP_8, GLFW.GLFW_KEY_KP_9:
                    yield (char) (keyCode - 272);
            default: yield (char) keyCode;
        };
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // TODO: Figure out later
    }
}
