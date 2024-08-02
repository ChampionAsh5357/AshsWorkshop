/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client.screen;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.screen.widget.V1SudokuGridWidget;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.screen.widget.SudokuGridWidget;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.SudokuGrid;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.grid.SudokuGridSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;

// TODO: Document
public class SudokuScreen extends Screen {

    private V1SudokuGridWidget v1GridWidget;
    private SudokuGridWidget gridWidget;

    private static final net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.grid.SudokuGrid DUMMY = new net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.grid.SudokuGrid(
            Holder.direct(
                    new SudokuGridSettings(9, List.of(
                            new SudokuGridSettings.InitialValue(1, 4, '2'),
                            new SudokuGridSettings.InitialValue(1, 5, '6'),
                            new SudokuGridSettings.InitialValue(1, 7, '7'),
                            new SudokuGridSettings.InitialValue(1, 9, '1'),

                            new SudokuGridSettings.InitialValue(2, 1, '6'),
                            new SudokuGridSettings.InitialValue(2, 2, '8'),
                            new SudokuGridSettings.InitialValue(2, 5, '7'),
                            new SudokuGridSettings.InitialValue(2, 8, '9'),

                            new SudokuGridSettings.InitialValue(3, 1, '1'),
                            new SudokuGridSettings.InitialValue(3, 2, '9'),
                            new SudokuGridSettings.InitialValue(3, 6, '4'),
                            new SudokuGridSettings.InitialValue(3, 7, '5'),

                            new SudokuGridSettings.InitialValue(4, 1, '8'),
                            new SudokuGridSettings.InitialValue(4, 2, '2'),
                            new SudokuGridSettings.InitialValue(4, 4, '1'),
                            new SudokuGridSettings.InitialValue(4, 8, '4'),

                            new SudokuGridSettings.InitialValue(5, 3, '4'),
                            new SudokuGridSettings.InitialValue(5, 4, '6'),
                            new SudokuGridSettings.InitialValue(5, 6, '2'),
                            new SudokuGridSettings.InitialValue(5, 7, '9'),

                            new SudokuGridSettings.InitialValue(6, 2, '5'),
                            new SudokuGridSettings.InitialValue(6, 6, '3'),
                            new SudokuGridSettings.InitialValue(6, 8, '2'),
                            new SudokuGridSettings.InitialValue(6, 9, '8'),

                            new SudokuGridSettings.InitialValue(7, 3, '9'),
                            new SudokuGridSettings.InitialValue(7, 4, '3'),
                            new SudokuGridSettings.InitialValue(7, 8, '7'),
                            new SudokuGridSettings.InitialValue(7, 9, '4'),

                            new SudokuGridSettings.InitialValue(8, 2, '4'),
                            new SudokuGridSettings.InitialValue(8, 5, '5'),
                            new SudokuGridSettings.InitialValue(8, 8, '3'),
                            new SudokuGridSettings.InitialValue(8, 9, '6'),

                            new SudokuGridSettings.InitialValue(9, 1, '7'),
                            new SudokuGridSettings.InitialValue(9, 3, '3'),
                            new SudokuGridSettings.InitialValue(9, 5, '1'),
                            new SudokuGridSettings.InitialValue(9, 6, '8')
                    ))
            )
    );

    public SudokuScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        this.v1GridWidget = this.addRenderableWidget(new V1SudokuGridWidget(this.font, SudokuGrid.constructTestGrid(), this.width / 2, this.height / 2, 16, 1, 1f));
        this.gridWidget = this.addRenderableWidget(new SudokuGridWidget(this.font, DUMMY, this.width / 2, this.height / 2, 16, 1, 1f));
        this.gridWidget.visible = false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_V) {
            this.v1GridWidget.visible = !this.v1GridWidget.visible;
            this.gridWidget.visible = !this.gridWidget.visible;
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
