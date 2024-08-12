/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.network.server;

import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Set;

public record ClientboundSendPreviouslyPlayedGrids(Set<Holder<SudokuGridSettings>> settings) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundSendPreviouslyPlayedGrids> TYPE = new CustomPacketPayload.Type<>(AshsWorkshop.fromMod("send_previously_played_grids"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSendPreviouslyPlayedGrids> STREAM_CODEC = StreamCodec.composite(
            SudokuGridSettings.STREAM_CODEC.apply(WorkshopCodecs.setStreamCodec()), ClientboundSendPreviouslyPlayedGrids::settings,
            ClientboundSendPreviouslyPlayedGrids::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
