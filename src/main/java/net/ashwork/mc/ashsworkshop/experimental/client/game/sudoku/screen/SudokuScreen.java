/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.screen;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.screen.widget.SudokuGridWidget;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGrid;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

// TODO: Document
public class SudokuScreen extends Screen {

    private final SudokuGrid grid;

    public SudokuScreen(Component title, SudokuGrid grid) {
        super(title);
        this.grid = grid;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new SudokuGridWidget(this.font, this.grid, this.width / 2, this.height / 2, 16, 1, 1f));
    }
}
