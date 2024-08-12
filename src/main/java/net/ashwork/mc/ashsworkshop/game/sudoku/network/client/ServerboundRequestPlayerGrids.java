package net.ashwork.mc.ashsworkshop.game.sudoku.network.client;

import io.netty.buffer.ByteBuf;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ServerboundRequestPlayerGrids implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundRequestPlayerGrids> TYPE = new CustomPacketPayload.Type<>(AshsWorkshop.fromMod("request_player_grids"));
    public static final ServerboundRequestPlayerGrids INSTANCE = new ServerboundRequestPlayerGrids();
    public static final StreamCodec<ByteBuf, ServerboundRequestPlayerGrids> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ServerboundRequestPlayerGrids() {}

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
