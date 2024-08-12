package net.ashwork.mc.ashsworkshop.game.sudoku.network;

import net.ashwork.mc.ashsworkshop.game.sudoku.network.client.ServerboundRequestPlayerGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.common.BiboundSendPlayerGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.server.ClientboundSendPreviouslyPlayedGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.saveddata.SudokuData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SudokuServerPayloadHandler {

    public static void onRequestPlayerGrids(ServerboundRequestPlayerGrids payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var serverPlayer = (ServerPlayer) context.player();
            var data = SudokuData.init(serverPlayer.serverLevel());

            switch (payload.request()) {
                case PREVIOUSLY_PLAYED_GRIDS ->
                        context.reply(new ClientboundSendPreviouslyPlayedGrids(data.getPlayedGrids(serverPlayer)));
                case SUDOKU_GRID -> {
                    context.reply(new BiboundSendPlayerGrid(data.getGrid(serverPlayer, payload.settings().orElseThrow())));
                }
                default -> throw new IllegalStateException("I have no idea how you got here.");
            }
        }).exceptionally(e -> {
            // TODO: Add network message
            context.disconnect(Component.literal("Fail on sudoku settings request"));
            return null;
        });
    }

    public static void onSendPlayerGrid(BiboundSendPlayerGrid payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var serverPlayer = (ServerPlayer) context.player();
            var data = SudokuData.init(serverPlayer.serverLevel());

            data.updateGrid(serverPlayer, payload.grid());
        }).exceptionally(e -> {
            // TODO: Add network message
            context.disconnect(Component.literal("Fail on sudoku update"));
            return null;
        });
    }
}
