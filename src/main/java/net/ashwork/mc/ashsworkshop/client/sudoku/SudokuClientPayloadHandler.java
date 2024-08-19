/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku;

import net.ashwork.mc.ashsworkshop.client.screen.WorkbenchScreen;
import net.ashwork.mc.ashsworkshop.client.sudoku.screen.SudokuScreen;
import net.ashwork.mc.ashsworkshop.client.sudoku.screen.SudokuSelectionScreen;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.common.BiboundSendPlayerGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.server.ClientboundSendPreviouslyPlayedGrids;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * A class that contains the handlers for client-bound payload.
 */
public class SudokuClientPayloadHandler {

    /**
     * Handles the {@link ClientboundSendPreviouslyPlayedGrids} payload.
     *
     * @param payload the payload instance
     * @param context the payload context
     */
    public static void onRequestPlayerGrids(ClientboundSendPreviouslyPlayedGrids payload, IPayloadContext context) {
        // Read in previous properties and set screen to sudoku selection
        boolean fullscreen = Minecraft.getInstance().screen instanceof WorkbenchScreen workbench && workbench.isFullscreen();
        Minecraft.getInstance().setScreen(new SudokuSelectionScreen(Component.empty(), payload.settings(), fullscreen));
    }

    /**
     * Handles the {@link BiboundSendPlayerGrid} payload.
     *
     * @param payload the payload instance
     * @param context the payload context
     */
    public static void onSendPlayerGrid(BiboundSendPlayerGrid payload, IPayloadContext context) {
        // Set screen to sudoku grid
        Minecraft.getInstance().setScreen(new SudokuScreen(Component.empty(), payload.grid()));
    }
}
