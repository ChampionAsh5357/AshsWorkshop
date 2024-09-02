/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.screen;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.client.ServerboundRequestPlayerGrids;
import net.ashwork.mc.ashsworkshop.init.AnalysisRegistrar;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.menu.WorkbenchMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

/**
 * The screen class for the workbench menu.
 */
public class WorkbenchScreen extends Screen implements MenuAccess<WorkbenchMenu> {

    /**
     * The border of the screen when not in fullscreen.
     */
    private static final ResourceLocation SCREEN_BORDER = AshsWorkshop.fromMod("textures/gui/workbench/screen_border.png");

    private final WorkbenchMenu menu;
    private int screenWidth;
    private int screenHeight;
    private int borderSize;
    private int taskbarSize;
    private boolean fullscreen;
    private boolean packetSent;

    private int leftPos;
    private int topPos;

    public WorkbenchScreen(WorkbenchMenu menu, Component title) {
        super(title);
        this.menu = menu;
        this.fullscreen = false;
    }

    @Override
    protected void init() {
        // Handle fullscreen mode
        if (this.fullscreen) {
            this.screenWidth = this.width;
            this.screenHeight = this.height;
            this.borderSize = 0;
            this.taskbarSize = 10;
        } else {
            this.screenWidth = 246;
            this.screenHeight = 134;
            this.borderSize = 5;
            this.taskbarSize = 5;
        }

        this.leftPos = (this.width - this.screenWidth) / 2 + this.borderSize;
        this.topPos = (this.height - this.screenHeight) / 2 + this.borderSize;

        // TODO: Make more dynamic later for multiple applications
        this.addRenderableWidget(this.createIcon(AnalysisRegistrar.SUDOKU.get(), AshsWorkshop.fromMod("workbench/icons/sudoku"), 16, this.leftPos, this.topPos, 24, Component.literal("Sudoku")));
    }

    // TODO: Should remove once all other screens are merged into this one
    public boolean isFullscreen() {
        return this.fullscreen;
    }

    @Override
    public WorkbenchMenu getMenu() {
        return this.menu;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);

        // Draw border if not in full screen
        if (!this.fullscreen) {
            graphics.blit(SCREEN_BORDER,
                    this.leftPos - this.borderSize, this.topPos - this.borderSize,
                    0, 0,
                    this.screenWidth + this.borderSize * 2, this.screenHeight + this.borderSize * 2
            );
        }

        // Fill in screen
        graphics.fill(this.leftPos, this.topPos, this.leftPos + this.screenWidth, this.topPos + this.screenHeight, 0xFF64B2D1);
        graphics.fill(this.leftPos, this.topPos + this.screenHeight - this.taskbarSize, this.leftPos + this.screenWidth, this.topPos + this.screenHeight, 0xFFAFB7BA);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Disable important input when updating page
        if (this.packetSent) {
            return false;
        }

        // Handle clicking on inidividual elements
        for (GuiEventListener guieventlistener : this.children()) {
            if (guieventlistener.mouseClicked(mouseX, mouseY, button)) {
                this.setFocused(guieventlistener);
                if (button == 0) {
                    this.setDragging(true);
                }

                return true;
            }
        }

        this.setFocused(null);
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Disable important input when updating page
        if (this.packetSent) {
            return false;
        }

        // Swap fullscreen
        // TODO: Make keybinding and button
        if (keyCode == GLFW.GLFW_KEY_F) {
            this.fullscreen = !this.fullscreen;
            this.rebuildWidgets();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    private static final ResourceLocation UNKNOWN_ICON = AshsWorkshop.fromMod("workbench/icons/unknown");

    private Icon createIcon(Analysis<?> analysis, ResourceLocation iconSprite, int iconSize, int x, int y, int size, MutableComponent message) {
        var seeIcon = Minecraft.getInstance().player.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER).hasAnalyzedResources(analysis);
        if (!seeIcon) {
            message = message.withStyle(ChatFormatting.OBFUSCATED);
        }
        return new Icon(seeIcon ? iconSprite : UNKNOWN_ICON, iconSize, x, y, size, message, seeIcon);
    }

    /**
     * A class that represents a clickable icon on the workbench screen.
     */
    public class Icon extends AbstractWidget {

        private final ResourceLocation iconSprite;
        private final int iconSize;
        private long lastClickTime;
        private final boolean clickable;

        private Icon(ResourceLocation iconSprite, int iconSize, int x, int y, int size, Component message, boolean clickable) {
            super(x, y, size, size, message);
            this.iconSprite = iconSprite;
            this.iconSize = iconSize;
            this.lastClickTime = 0L;
            this.clickable = clickable;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            // Draw selected border when clicked
            if (this.isFocused()) {
                graphics.hLine(this.getX(), this.getX() + this.width, this.getY(), 0xFF0A3D69);
                graphics.hLine(this.getX(), this.getX() + this.width, this.getY() + this.height - 1, 0xFF0A3D69);
                graphics.vLine(this.getX(), this.getY(), this.getY() + this.height, 0xFF0A3D69);
                graphics.vLine(this.getX() + this.width, this.getY(), this.getY() + this.height, 0xFF0A3D69);
            }

            // Draw sprite
            graphics.blitSprite(this.iconSprite,
                    this.getX() + (this.width - this.iconSize) / 2, this.getY() + (this.height - this.iconSize) / 4, 10,
                    this.iconSize, this.iconSize
            );
            graphics.pose().pushPose();

            // TODO: Fix text scaling and location
            // TODO: Figure out better text rendering at small sizes, Mojang's font is not really great for it.

            // Get text box
            int textBoxWidth = this.width - 4;
            int textBoxHeight = 4;

            // Handle text scaling
            FormattedCharSequence centerText = this.getMessage().getVisualOrderText();
            float textWidth = WorkbenchScreen.this.font.width(centerText) - 1;
            float textHeight = WorkbenchScreen.this.font.lineHeight - 2;
            graphics.pose().translate(this.getX() + this.width / 2f, this.getY() + (float) (this.height - this.iconSize) / 4 + this.iconSize + (float) textBoxHeight / 2, 0f);
            float centerScalingFactor = textBoxHeight / textHeight;
            if (textWidth >= textBoxWidth) {
                centerScalingFactor = Math.min(textBoxWidth / textWidth, centerScalingFactor);
            }
            graphics.pose().scale(centerScalingFactor, centerScalingFactor, 1f);
            graphics.drawString(WorkbenchScreen.this.font, centerText, -textWidth / 2f, -textHeight / 2f, 0xFFFFFFFF, false);
            graphics.pose().popPose();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            // TODO: Properly handle accessibility
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            if (!this.clickable) {
                return;
            }

            if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                long clickTime = Util.getMillis();
                if (clickTime - this.lastClickTime < 250L) {
                    // Double click occurred
                    PacketDistributor.sendToServer(new ServerboundRequestPlayerGrids());
                    WorkbenchScreen.this.packetSent = true;
                }
                this.lastClickTime = clickTime;
            }
        }
    }
}
