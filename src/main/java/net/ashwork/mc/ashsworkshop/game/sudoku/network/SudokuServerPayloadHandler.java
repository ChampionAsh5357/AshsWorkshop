/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.network;

import net.ashwork.mc.ashsworkshop.game.sudoku.network.client.ServerboundRequestPlayerGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.common.BiboundSendPlayerGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.server.ClientboundSendPreviouslyPlayedGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.saveddata.SudokuData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SudokuServerPayloadHandler {

    public static void onRequestPlayerGrids(ServerboundRequestPlayerGrids payload, IPayloadContext context) {
        var serverPlayer = (ServerPlayer) context.player();
        var data = SudokuData.init(serverPlayer);

        switch (payload.request()) {
            case PREVIOUSLY_PLAYED_GRIDS ->
                    context.reply(new ClientboundSendPreviouslyPlayedGrids(data.getPlayedGrids()));
            case SUDOKU_GRID ->
                    context.reply(new BiboundSendPlayerGrid(data.getGrid(payload.settings().orElseThrow())));
            default -> throw new IllegalStateException("I have no idea how you got here.");
        }
    }

    public static void onSendPlayerGrid(BiboundSendPlayerGrid payload, IPayloadContext context) {
        var serverPlayer = (ServerPlayer) context.player();
        var data = SudokuData.init(serverPlayer);

        data.updateGrid(payload.grid());
    }
}
