/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.network.server;

import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;

public record ClientboundSendPreviouslyPlayedGrids(Map<Holder<SudokuGridSettings>, SudokuGridSettings.SolutionState> settings) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundSendPreviouslyPlayedGrids> TYPE = new CustomPacketPayload.Type<>(AshsWorkshop.fromMod("send_previously_played_grids"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSendPreviouslyPlayedGrids> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, SudokuGridSettings.STREAM_CODEC, SudokuGridSettings.SolutionState.STREAM_CODEC), ClientboundSendPreviouslyPlayedGrids::settings,
            ClientboundSendPreviouslyPlayedGrids::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
