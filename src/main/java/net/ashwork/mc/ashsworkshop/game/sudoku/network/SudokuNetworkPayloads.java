/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.network;

import net.ashwork.mc.ashsworkshop.client.sudoku.SudokuClientPayloadHandler;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.client.ServerboundRequestPlayerGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.common.BiboundSendPlayerGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.server.ClientboundSendPreviouslyPlayedGrids;
import net.ashwork.mc.ashsworkshop.network.WrappedPayloadHandler;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class SudokuNetworkPayloads {

    public static void register(PayloadRegistrar registrar) {
        registrar.playToServer(
                ServerboundRequestPlayerGrids.TYPE,
                ServerboundRequestPlayerGrids.STREAM_CODEC,
                SudokuServerPayloadHandler::onRequestPlayerGrids
        );
        registrar.playToClient(
                ClientboundSendPreviouslyPlayedGrids.TYPE,
                ClientboundSendPreviouslyPlayedGrids.STREAM_CODEC,
                new WrappedPayloadHandler<>(() -> SudokuClientPayloadHandler::onRequestPlayerGrids)
        );
        registrar.playBidirectional(
                BiboundSendPlayerGrid.TYPE,
                BiboundSendPlayerGrid.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        new WrappedPayloadHandler<>(() -> SudokuClientPayloadHandler::onSendPlayerGrid),
                        SudokuServerPayloadHandler::onSendPlayerGrid
                )
        );
    }
}
