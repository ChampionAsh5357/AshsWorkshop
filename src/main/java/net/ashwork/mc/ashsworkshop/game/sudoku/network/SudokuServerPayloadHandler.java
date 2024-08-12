package net.ashwork.mc.ashsworkshop.game.sudoku.network;

import net.ashwork.mc.ashsworkshop.game.sudoku.network.client.ServerboundRequestPlayerGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.server.ClientboundSendPlayerGrids;
import net.ashwork.mc.ashsworkshop.game.sudoku.saveddata.SudokuData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SudokuServerPayloadHandler {

    public static void onRequestPlayerGrids(ServerboundRequestPlayerGrids payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.reply(new ClientboundSendPlayerGrids(SudokuData.init((ServerLevel) context.player().level()).getPlayedGrids((ServerPlayer) context.player())));
        }).exceptionally(e -> {
            // TODO: Add network message
            context.disconnect(Component.literal("Fail on sudoku settings request"));
            return null;
        });
    }
}
