package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.experimental.init.ConstraintTypeRegistrar;

import java.util.function.BiConsumer;

public class ColumnConstraint implements SudokuConstraint {

    public static final ColumnConstraint INSTANCE = new ColumnConstraint();
    public static final MapCodec<ColumnConstraint> CODEC = MapCodec.unit(INSTANCE);

    private ColumnConstraint() {}

    @Override
    public void apply(SudokuGridSettings settings, int rowIdx, int columnIdx, BiConsumer<Integer, Integer> constraint) {
        for (int idx = 0; idx < settings.gridLength(); idx++) {
            constraint.accept(rowIdx, idx);
        }
    }

    @Override
    public Type<?> type() {
        return ConstraintTypeRegistrar.COLUMN.get();
    }
}
