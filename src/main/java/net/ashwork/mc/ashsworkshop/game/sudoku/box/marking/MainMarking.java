/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class MainMarking extends AbstractSudokuMarking {

    public static final MapCodec<MainMarking> CODEC = WorkshopCodecs.SUDOKU_VALUE
            .optionalFieldOf("value")
            .xmap(
                    opt -> opt.map(MainMarking::new).orElseGet(MainMarking::new),
                    marking -> Optional.ofNullable(marking.getValue())
            );
    public static final StreamCodec<ByteBuf, MainMarking> STREAM_CODEC = WorkshopCodecs.CHAR_STREAM_CODEC.map(MainMarking::new, MainMarking::getValue);

    @Nullable
    private Character value;

    public MainMarking() {
        this.value = null;
    }

    private MainMarking(@Nullable Character value) {
        this.value = value;
    }

    @Nullable
    public Character getValue() {
        return this.value;
    }

    @Override
    public void mark(char value) {
        this.value = Objects.equals(this.value, value) ? null : value;
    }

    @Override
    protected void clearMark() {
        this.value = null;
    }

    @Override
    public boolean containsData() {
        return this.value != null;
    }

    @Override
    public Type<?> type() {
        return MarkingRegistrar.MAIN.get();
    }

    @Override
    public String toString() {
        return "MainMarking{" + this.value + "}";
    }
}
