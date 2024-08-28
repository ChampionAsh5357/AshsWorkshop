/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.network.server;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record ClientboundUpdateAnalyzedResources(Set<ResourceLocation> analyzedResources, boolean clear) implements CustomPacketPayload {

    public ClientboundUpdateAnalyzedResources(Set<ResourceLocation> analyzedResources) {
        this(analyzedResources, false);
    }

    public static final Type<ClientboundUpdateAnalyzedResources> TYPE = new Type<>(AshsWorkshop.fromMod("update_analyzed_resources"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateAnalyzedResources> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.apply(WorkshopCodecs.setStreamCodec()), ClientboundUpdateAnalyzedResources::analyzedResources,
            ByteBufCodecs.BOOL, ClientboundUpdateAnalyzedResources::clear,
            ClientboundUpdateAnalyzedResources::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
