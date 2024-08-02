/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

// TODO: Document
public class TestMovableWidget extends AbstractWidget {

    public static final ResourceLocation EXAMPLE = ResourceLocation.withDefaultNamespace("gamemode_switcher/slot");

    public TestMovableWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(EXAMPLE, this.getX(), this.getY(), this.width, this.height);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setX(this.getX() + (int) dragX);
        this.setY(this.getY() + (int) dragY);
    }
}
