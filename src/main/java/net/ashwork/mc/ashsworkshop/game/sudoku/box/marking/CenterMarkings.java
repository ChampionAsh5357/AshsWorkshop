/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public class CenterMarkings extends AbstractMultiMarkings<Character> {

    public static final MapCodec<CenterMarkings> CODEC = multiMarkingCodec(WorkshopCodecs.SUDOKU_VALUE, CenterMarkings::new);
    public static final StreamCodec<ByteBuf, CenterMarkings> STREAM_CODEC = multiMarkingStreamCodec(WorkshopCodecs.CHAR_STREAM_CODEC, CenterMarkings::new);

    public CenterMarkings() {
        super();
    }

    public CenterMarkings(List<Character> values) {
        super(values);
    }

    @Override
    public Type<?> type() {
        return MarkingRegistrar.CENTER.get();
    }

    @Override
    protected Character toType(char value) {
        return value;
    }
}
