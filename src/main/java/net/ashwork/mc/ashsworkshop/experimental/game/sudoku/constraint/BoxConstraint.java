package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.experimental.init.ConstraintTypeRegistrar;

import java.util.function.BiConsumer;

// TODO: Update to custom boxes later
public record BoxConstraint(int rowSize, int columnSize) implements SudokuConstraint {

    private static final MapCodec<BoxConstraint> SQUARE_SIZE_CODEC = Codec.INT.fieldOf("size")
            .xmap(BoxConstraint::new, BoxConstraint::rowSize);
    private static final MapCodec<BoxConstraint> RECTANGLE_SIZE_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("row_size").forGetter(BoxConstraint::rowSize),
                    Codec.INT.fieldOf("column_size").forGetter(BoxConstraint::columnSize)
            ).apply(instance, BoxConstraint::new)
    );
    public static final MapCodec<BoxConstraint> CODEC = Codec.mapEither(RECTANGLE_SIZE_CODEC, SQUARE_SIZE_CODEC).xmap(
            Either::unwrap,
            Either::left
    );

    public BoxConstraint(int size) {
        this(size, size);
    }

    @Override
    public void apply(SudokuGridSettings settings, int rowIdx, int columnIdx, BiConsumer<Integer, Integer> constraint) {
        int boxRowIdx = (rowIdx / this.rowSize) * this.rowSize;
        int boxColumnIdx = (columnIdx / this.columnSize) * this.columnSize;
        for (int idx = 0; idx < settings.gridLength(); idx++) {
            constraint.accept(boxRowIdx + (idx / this.columnSize), boxColumnIdx + (idx % this.rowSize));
        }
    }

    @Override
    public boolean validate(SudokuGridSettings settings) {
        return this.rowSize * this.columnSize == settings.gridLength();
    }

    @Override
    public Type<?> type() {
        return ConstraintTypeRegistrar.BOX.get();
    }
}
