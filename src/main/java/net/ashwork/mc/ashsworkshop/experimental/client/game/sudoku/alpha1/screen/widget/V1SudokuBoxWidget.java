/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.screen.widget;

import it.unimi.dsi.fastutil.ints.Int2CharArrayMap;
import net.ashwork.mc.ashsworkshop.experimental.client.ExperimentalAshsWorkshopClient;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking.MarkingClientHandler;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.SudokuBox;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.SudokuMarking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

// TODO: Document
public class V1SudokuBoxWidget extends AbstractWidget {

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

    public V1SudokuBoxWidget(Font font, SudokuBox box, int x, int y, int xIdx, int yIdx, int length, int selectedBorder, float margin) {
        super(x, y, length, length, Component.empty());
        this.font = font;
        this.box = box;
        this.xIdx = xIdx;
        this.yIdx = yIdx;
        this.selectedBorder = selectedBorder;
        this.margin = margin;
    }

    public boolean locked() {
        return this.box.locked();
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
        return this.box.value();
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
        if (this.box.value() != null && this.invalidValues.containsValue(this.box.value().charValue())) {
            if (this.selected) {
                guiGraphics.fill(this.getX() + this.selectedBorder, this.getY() + this.selectedBorder, this.getX() + this.getWidth() - this.selectedBorder, this.getY() + this.getHeight() - this.selectedBorder, 0X7FDC574D);
            } else {
                guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0X7FDC574D);
            }
        }

        var renderLayers = ExperimentalAshsWorkshopClient.instance().getMarkingManager().getRenderLayers();
        for (var layer : MarkingClientHandler.RenderLayer.values()) {
            var handlers = renderLayers.get(layer);
            if (handlers == null) {
                continue;
            }

            boolean stopRendering = false;
            for (var handler : handlers) {
                stopRendering |= this.renderLayer(handler, guiGraphics);
            }
            if (stopRendering) {
                break;
            }
        }
    }

    private <T extends SudokuMarking> boolean renderLayer(MarkingClientHandler<T> handler, GuiGraphics guiGraphics) {
        T marking = this.box.getMark(handler.type());
        if (marking == null) {
            return false;
        }
        return handler.render(marking, guiGraphics, this, this.font, this.invalidValues, this.box.locked(), this.selectedBorder, this.margin) == MarkingClientHandler.RenderState.STOP;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.locked()) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
            // Remove values
            var interactionLayers = ExperimentalAshsWorkshopClient.instance().getMarkingManager().getInteractionLayers();
            for (int i = 0; i < interactionLayers.size(); i++) {
                for (var handler : interactionLayers.get(i).second()) {
                    if (handler.canModifyMarking(modifiers) && this.box.clearMark(handler.type())) {
                        return true;
                    }
                }
            }
            this.box.clearMarks();

            return true;
        }

        var codePoint = keyToPoint(keyCode);
        if (codePoint >= '1' && codePoint <= '9') {
            // Set values
            var interactionLayers = ExperimentalAshsWorkshopClient.instance().getMarkingManager().getInteractionLayers();
            for (int i = interactionLayers.size() - 1; i >= 0; i--) {
                for (var handler : interactionLayers.get(i).second()) {
                    if (handler.canModifyMarking(modifiers)) {
                        this.box.mark(handler.type(), codePoint);
                        return true;
                    }
                }
            }

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
