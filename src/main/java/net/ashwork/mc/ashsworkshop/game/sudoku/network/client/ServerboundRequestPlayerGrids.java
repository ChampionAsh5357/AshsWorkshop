/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.network.client;

import io.netty.buffer.ByteBuf;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Optional;

public record ServerboundRequestPlayerGrids(Request request, Optional<Holder<SudokuGridSettings>> settings) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundRequestPlayerGrids> TYPE = new CustomPacketPayload.Type<>(AshsWorkshop.fromMod("request_player_grids"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestPlayerGrids> STREAM_CODEC = StreamCodec.composite(
            Request.STREAM_CODEC, ServerboundRequestPlayerGrids::request,
            SudokuGridSettings.STREAM_CODEC.apply(ByteBufCodecs::optional), ServerboundRequestPlayerGrids::settings,
            ServerboundRequestPlayerGrids::new
    );

    public ServerboundRequestPlayerGrids() {
        this(Request.PREVIOUSLY_PLAYED_GRIDS, Optional.empty());
    }

    public ServerboundRequestPlayerGrids(Holder<SudokuGridSettings> settings) {
        this(Request.SUDOKU_GRID, Optional.of(settings));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Request {
        PREVIOUSLY_PLAYED_GRIDS,
        SUDOKU_GRID;

        public static final StreamCodec<ByteBuf, Request> STREAM_CODEC = ByteBufCodecs.idMapper(id -> Request.values()[id], Request::ordinal);
    }
}
