package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public interface SudokuMarking {

    void mark(char value);

    boolean clear();

    boolean containsData();

    SudokuMarking.Type<?> type();

    record Type<T extends SudokuMarking>(Supplier<T> factory, MapCodec<T> codec) {}
}
