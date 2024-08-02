package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking;

import com.mojang.serialization.MapCodec;

public interface SudokuMarking {

    void markValue(Character value);

    boolean hasData();

    boolean clearValue();

    SudokuMarking.Type type();

    SudokuMarking copy();

    record Type(MapCodec<? extends SudokuMarking> codec) {}
}
