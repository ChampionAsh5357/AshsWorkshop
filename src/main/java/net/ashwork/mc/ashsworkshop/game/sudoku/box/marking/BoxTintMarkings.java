/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.box.marking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public class BoxTintMarkings extends AbstractMultiMarkings<Integer> {

    public static final MapCodec<BoxTintMarkings> CODEC = multiMarkingCodec(Codec.INT, BoxTintMarkings::new);
    public static final StreamCodec<ByteBuf, BoxTintMarkings> STREAM_CODEC = multiMarkingStreamCodec(ByteBufCodecs.VAR_INT, BoxTintMarkings::new);


    public BoxTintMarkings() {
        super();
    }

    public BoxTintMarkings(List<Integer> values) {
        super(values);
    }

    @Override
    public Type<?> type() {
        return MarkingRegistrar.BOX_TINT.get();
    }

    @Override
    protected Integer toType(char value) {
        return 0; // TODO: Create index map
    }
}
