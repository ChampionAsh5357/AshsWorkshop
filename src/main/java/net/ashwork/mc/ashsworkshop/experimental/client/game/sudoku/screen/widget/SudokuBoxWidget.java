/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.screen.widget;

import it.unimi.dsi.fastutil.ints.Int2CharArrayMap;
import net.ashwork.mc.ashsworkshop.experimental.client.ExperimentalAshsWorkshopClient;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.SudokuBox;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
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

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // TODO: Render white background or set layer color
        // TODO: Render invalid value color
        // TODO: Render Aesthetics
        // TODO: Render Value if set
        // TODO: Render markings if value isn't set

        if (this.selected) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF69C8EC);
            guiGraphics.fill(this.getX() + this.selectedBorder, this.getY() + this.selectedBorder, this.getX() + this.getWidth() - this.selectedBorder, this.getY() + this.getHeight() - this.selectedBorder, 0xFFFFFFFF);
        } else {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFFFFFFFF);
        }
        if (this.box.mainValue() != null && this.invalidValues.containsValue(this.box.mainValue().charValue())) {
            if (this.selected) {
                guiGraphics.fill(this.getX() + this.selectedBorder, this.getY() + this.selectedBorder, this.getX() + this.getWidth() - this.selectedBorder, this.getY() + this.getHeight() - this.selectedBorder, 0X7FDC574D);
            } else {
                guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0X7FDC574D);
            }
        }

        ExperimentalAshsWorkshopClient.instance().markingRendererHandler().renderLayers(
                guiGraphics, this.box, this.font, this.invalidValues::containsValue,
                this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                this.selectedBorder, this.margin, this.box.isLocked()
        );
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.locked()) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
            // Remove values
            if (!ExperimentalAshsWorkshopClient.instance().markingInteractionHandler().applyModifiers(modifiers, this.box::clear, false)) {
                this.box.clearAll();
            }

            return true;
        }

        var codePoint = keyToPoint(keyCode);
        if (codePoint >= '1' && codePoint <= '9' && !this.box.isLocked()) {
            ExperimentalAshsWorkshopClient.instance().markingInteractionHandler().applyModifiers(modifiers, type -> {
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
