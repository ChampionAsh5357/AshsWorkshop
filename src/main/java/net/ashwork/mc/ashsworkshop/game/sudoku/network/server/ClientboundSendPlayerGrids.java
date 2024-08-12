package net.ashwork.mc.ashsworkshop.game.sudoku.network.server;

import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashSet;
import java.util.Set;

public record ClientboundSendPlayerGrids(Set<Holder<SudokuGridSettings>> settings) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundSendPlayerGrids> TYPE = new CustomPacketPayload.Type<>(AshsWorkshop.fromMod("send_player_grids"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSendPlayerGrids> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(
                    WorkshopRegistries.SUDOKU_GRID_KEY).apply(codec -> ByteBufCodecs.collection(HashSet::new, codec)
            ), ClientboundSendPlayerGrids::settings,
            ClientboundSendPlayerGrids::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
