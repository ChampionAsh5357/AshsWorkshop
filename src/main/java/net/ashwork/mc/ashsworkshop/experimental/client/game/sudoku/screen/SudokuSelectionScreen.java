package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.screen;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGrid;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class SudokuSelectionScreen extends Screen {

    private static final ResourceLocation SCREEN_BORDER = AshsWorkshop.fromMod("textures/gui/workbench/screen_border.png");

    private int screenWidth;
    private int screenHeight;
    private int borderSize;
    private boolean fullscreen;

    private int leftPos;
    private int topPos;

    public SudokuSelectionScreen(Component title, boolean fullscreen) {
        super(title);
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_F) {
            this.fullscreen = !this.fullscreen;
            this.rebuildWidgets();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    class SudokuList extends ObjectSelectionList<SudokuList.SudokuEntry> {

        public SudokuList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);

            this.minecraft.level.registryAccess().registryOrThrow(ExperimentalWorkshopRegistries.SUDOKU_GRID_KEY).holders()
                    .forEach(settings -> this.addEntry(new SudokuEntry(settings)));
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
            private long lastClickTime;

            SudokuEntry(Holder<SudokuGridSettings> settings) {
                this.settings = settings;
                this.lastClickTime = 0L;
            }

            @Override
            public Component getNarration() {
                return Component.empty();
            }

            @Override
            public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
                var lineHeight = SudokuSelectionScreen.this.font.lineHeight;
                this.settings.value().attribution().ifPresent(info -> {
                    graphics.drawString(SudokuSelectionScreen.this.font, info.title(), left + 4, top + (height - lineHeight + 1) / 2 - 5, 0xFFFFFFFF, false);
                    graphics.drawString(SudokuSelectionScreen.this.font, "by " + info.author(), left + 4, top + (height - lineHeight + 1) / 2 + 5, 0xFFAAAAAA, false);
                });
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                    long clickTime = Util.getMillis();
                    if (clickTime - this.lastClickTime < 250L) {
                        // Double click occurred
                        SudokuList.this.minecraft.setScreen(new SudokuScreen(Component.empty(), new SudokuGrid(this.settings)));
                    }
                    this.lastClickTime = clickTime;
                }

                return super.mouseClicked(mouseX, mouseY, button);
            }

            @Override
            public void renderBack(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
                SudokuList.this.renderSelection(guiGraphics, top, width, height, 0x2F2E2E2E, 0x2F7A7A7A);
            }
        }
    }
}