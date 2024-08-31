/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.screen;

import net.ashwork.mc.ashsworkshop.client.sudoku.screen.widget.SudokuGridWidget;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.common.BiboundSendPlayerGrid;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * The screen seen when solving a sudoku puzzle.
 *
 * TODO: This should be removed and merged with the workbench screen. That's the whole point of the widgets.
 */
public class SudokuScreen extends Screen {

    private final SudokuGrid grid;

    public SudokuScreen(Component title, SudokuGrid grid) {
        super(title);
        this.grid = grid;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new SudokuGridWidget(this.font, this.grid, this.width / 2, this.height / 2, 16, 1, 1f, this::onClose));
    }

    @Override
    public void onClose() {
        // Update grid status on close
        super.onClose();
        PacketDistributor.sendToServer(new BiboundSendPlayerGrid(this.grid));
    }
}
