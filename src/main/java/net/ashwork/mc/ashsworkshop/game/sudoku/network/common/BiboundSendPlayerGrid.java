package net.ashwork.mc.ashsworkshop.game.sudoku.network.common;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGrid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record BiboundSendPlayerGrid(SudokuGrid grid) implements CustomPacketPayload {

    public static final Type<BiboundSendPlayerGrid> TYPE = new Type<>(AshsWorkshop.fromMod("send_player_grid"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BiboundSendPlayerGrid> STREAM_CODEC = StreamCodec.composite(
            SudokuGrid.STREAM_CODEC, BiboundSendPlayerGrid::grid,
            BiboundSendPlayerGrid::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
