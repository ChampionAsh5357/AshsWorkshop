/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.screen;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.client.ServerboundRequestPlayerGrids;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

/**
 * The selection screen where all available sudokus that can be completed are displayed.
 *
 * TODO: Turn into a widget and embed inside workbench screen. Lots of duplicate code
 */
public class SudokuSelectionScreen extends Screen {

    private static final ResourceLocation SCREEN_BORDER = AshsWorkshop.fromMod("textures/gui/workbench/screen_border.png");

    private final Map<Holder<SudokuGridSettings>, SudokuGridSettings.SolutionState> viewable;
    private int screenWidth;
    private int screenHeight;
    private int borderSize;
    private boolean fullscreen;
    private boolean packetSent;

    private int leftPos;
    private int topPos;

    public SudokuSelectionScreen(Component title, Map<Holder<SudokuGridSettings>, SudokuGridSettings.SolutionState> viewable, boolean fullscreen) {
        super(title);
        this.viewable = viewable;
        this.fullscreen = fullscreen;
    }

    @Override
    protected void init() {
        super.init();

        if (this.fullscreen) {
            this.screenWidth = this.width;
            this.screenHeight = this.height;
            this.borderSize = 0;
        } else {
            this.screenWidth = 246;
            this.screenHeight = 134;
            this.borderSize = 5;
        }

        this.leftPos = (this.width - this.screenWidth) / 2 + this.borderSize;
        this.topPos = (this.height - this.screenHeight) / 2 + this.borderSize;

        var list = this.addRenderableWidget(new SudokuList(this.minecraft, this.screenWidth, this.screenHeight, this.topPos, 24));
        list.setX(this.leftPos);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);

        if (!this.fullscreen) {
            graphics.blit(SCREEN_BORDER,
                    this.leftPos - this.borderSize, this.topPos - this.borderSize,
                    0, 0,
                    this.screenWidth + this.borderSize * 2, this.screenHeight + this.borderSize * 2
            );
        }

        // Fill in screen
        graphics.fill(this.leftPos, this.topPos, this.leftPos + this.screenWidth, this.topPos + this.screenHeight, 0xFF64B2D1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.packetSent) {
            return false;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.packetSent) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_F) {
            this.fullscreen = !this.fullscreen;
            this.rebuildWidgets();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    class SudokuList extends ObjectSelectionList<SudokuList.SudokuEntry> {

        public SudokuList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);

            // Loop through available grids and display status appropriately
            this.minecraft.level.registryAccess().registryOrThrow(WorkshopRegistries.SUDOKU_GRID_KEY).holders()
                    .forEach(settings -> {
                        // Only show unlocked entries
                        var state = SudokuSelectionScreen.this.viewable.get(settings);
                        if (state != null) {
                            this.addEntry(new SudokuEntry(settings, state));
                        }
                    });
        }

        @Override
        protected void renderListSeparators(GuiGraphics guiGraphics) {
        }

        @Override
        public int getRowWidth() {
            return SudokuSelectionScreen.this.fullscreen ? (SudokuSelectionScreen.this.screenWidth - 27) : super.getRowWidth();
        }

        @Override
        protected int getDefaultScrollbarPosition() {
            return SudokuSelectionScreen.this.fullscreen ? (SudokuSelectionScreen.this.screenWidth - 6) : (super.getDefaultScrollbarPosition() - 3);
        }

        class SudokuEntry extends ObjectSelectionList.Entry<SudokuEntry> {

            private final Holder<SudokuGridSettings> settings;
            private final SudokuGridSettings.SolutionState state;
            private long lastClickTime;

            SudokuEntry(Holder<SudokuGridSettings> settings, SudokuGridSettings.SolutionState state) {
                this.settings = settings;
                this.state = state;
                this.lastClickTime = 0L;
            }

            @Override
            public Component getNarration() {
                // TODO: Update to a proper narration
                return Component.empty();
            }

            @Override
            public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
                var lineHeight = SudokuSelectionScreen.this.font.lineHeight;
                // Draw metadata of grid text
                this.settings.value().attribution().ifPresent(info -> {
                    graphics.drawString(SudokuSelectionScreen.this.font, info.title(), left + 4, top + (height - lineHeight + 1) / 2 - 5, 0xFFFFFFFF, false);
                    graphics.drawString(SudokuSelectionScreen.this.font, "by " + info.author(), left + 4, top + (height - lineHeight + 1) / 2 + 5, 0xFFAAAAAA, false);
                });
                var progressText = this.state.getDisplayName();
                var textLength = SudokuSelectionScreen.this.font.width(progressText);
                graphics.drawString(SudokuSelectionScreen.this.font, progressText, left + width - textLength - 4, top + (height - lineHeight + 1) / 2, 0xFFFFFFFF, false);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                    long clickTime = Util.getMillis();
                    if (clickTime - this.lastClickTime < 250L) {
                        // Double click occurred
                        if (this.state.getServerData()) {
                            PacketDistributor.sendToServer(new ServerboundRequestPlayerGrids(settings));
                            SudokuSelectionScreen.this.packetSent = true;
                        } else {
                            SudokuList.this.minecraft.setScreen(new SudokuScreen(Component.empty(), new SudokuGrid(this.settings)));
                        }
                    }
                    this.lastClickTime = clickTime;
                }

                return super.mouseClicked(mouseX, mouseY, button);
            }

            @Override
            public void renderBack(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
                // Make this a transparent overlay
                SudokuList.this.renderSelection(guiGraphics, top, width, height, 0x2F2E2E2E, 0x2F7A7A7A);
            }
        }
    }
}
