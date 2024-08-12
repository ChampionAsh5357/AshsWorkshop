package net.ashwork.mc.ashsworkshop.game.sudoku.network;

import net.ashwork.mc.ashsworkshop.client.sudoku.SudokuClientPayloadHandler;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.client.ServerboundRequestPlayerGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.server.ClientboundSendPlayerGrids;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class SudokuNetworkPayloads {

    public static void register(PayloadRegistrar registrar) {
        registrar.playToServer(
                ServerboundRequestPlayerGrids.TYPE,
                ServerboundRequestPlayerGrids.STREAM_CODEC,
                SudokuServerPayloadHandler::onRequestPlayerGrids
        );
        registrar.playToClient(
                ClientboundSendPlayerGrids.TYPE,
                ClientboundSendPlayerGrids.STREAM_CODEC,
                SudokuClientPayloadHandler::onRequestPlayerGrids
        );
    }
}
