package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.grid;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record SudokuGridSettings(int gridLength, List<InitialValue> initialValues) {

    public static final Codec<SudokuGridSettings> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.intRange(4, Integer.MAX_VALUE).fieldOf("grid_length").forGetter(settings -> settings.gridLength),
                    InitialValue.CODEC.listOf().fieldOf("initial_values").forGetter(settings -> settings.initialValues)
            ).apply(instance, SudokuGridSettings::new)
    );
    public static final Codec<Holder<SudokuGridSettings>> CODEC = RegistryFileCodec.create(ExperimentalWorkshopRegistries.SUDOKU_GRID_KEY, DIRECT_CODEC);

    public SudokuGridSettings(int gridLength, List<InitialValue> initialValues) {
        this.gridLength = gridLength;
        var mutable = new ArrayList<>(initialValues);
        mutable.sort(Comparator.comparingInt(value -> value.index(this.gridLength)));
        this.initialValues = ImmutableList.copyOf(mutable);

        IntSet validator = new IntOpenHashSet();
        for (var value : this.initialValues) {
            if (value.row() > this.gridLength || value.column() > this.gridLength) {
                throw new IllegalArgumentException("Initial value (" + value.row() + ", " + value.column() + ") is not within grid " + this.gridLength);
            } else if (!validator.add(value.row() * this.gridLength + value.column())) {
                throw new IllegalArgumentException("Duplicate initial value position (" + value.row() + ", " + value.column() + ")");
            }
        }
    }

    public record InitialValue(int row, int column, Character value) {
        public static final Codec<InitialValue> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("row").forGetter(InitialValue::row),
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("column").forGetter(InitialValue::column),
                        WorkshopCodecs.SUDOKU_VALUE.fieldOf("value").forGetter(InitialValue::value)
                ).apply(instance, InitialValue::new)
        );

        public int columnIndex() {
            return this.column - 1;
        }

        public int rowIndex() {
            return this.row - 1;
        }

        public int index(int gridLength) {
            return this.rowIndex() * gridLength + this.columnIndex();
        }
    }
}
