package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public interface SudokuMarking<T> {

    void mark(T value);

    boolean clear();

    boolean containsData();

    SudokuMarking.Type<T, ?> type();

    record Type<T, M extends SudokuMarking<T>>(Supplier<M> factory, MapCodec<M> codec) {}
}
