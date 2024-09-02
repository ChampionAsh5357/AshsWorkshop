/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

/**
 * A marking made by the player on the sudoku grid.
 */
public interface SudokuMarking {

    /**
     * Marks the grid with the associated input. May unset data with the marked value is the same as that provided.
     *
     * @param value the value being marked
     */
    void mark(char value);

    /**
     * Clears the marked data.
     *
     * @return {@code true} if there was data to clear, {@code false} otherwise
     */
    boolean clear();

    /**
     * {@return {@code true} if there was data within the marking, {@code false} otherwise}
     */
    boolean containsData();

    /**
     * {@return the type of the marking}
     */
    SudokuMarking.Type<?> type();

    record Type<T extends SudokuMarking>(Supplier<T> factory, MapCodec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {}
}
