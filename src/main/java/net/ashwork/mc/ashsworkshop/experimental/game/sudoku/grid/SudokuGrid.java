package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.SudokuBox;
import net.minecraft.core.Holder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class SudokuGrid {

    public static final Codec<SudokuGrid> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    SudokuGridSettings.CODEC.fieldOf("settings").forGetter(SudokuGrid::getSettings),
                    BoxIndex.CODEC.listOf().optionalFieldOf("boxes").xmap(
                            opt -> opt.orElseGet(Collections::emptyList),
                            idx -> idx.isEmpty() ? Optional.empty() : Optional.of(idx)
                    ).forGetter(SudokuGrid::getBoxIndices)
            ).apply(instance, SudokuGrid::new)
    );

    private final Holder<SudokuGridSettings> settings;
    private final List<SudokuBox> boxes;

    public SudokuGrid(Holder<SudokuGridSettings> settings) {
        this.settings = settings;
        this.boxes = new ArrayList<>(this.getGridLength() * this.getGridLength());

        Iterator<SudokuGridSettings.InitialValue> iter = settings.value().initialValues().iterator();
        var initialValue = iter.next();
        for (int i = 0; i < this.getGridLength() * this.getGridLength(); i++) {
            if (initialValue != null && initialValue.index(this.getGridLength()) == i) {
                this.boxes.add(new SudokuBox(initialValue.value()));
                initialValue = iter.hasNext() ? iter.next() : null;
            } else {
                this.boxes.add(new SudokuBox());
            }
        }

        if (iter.hasNext()) {
            throw new IllegalStateException("Initial value(s) was not completely set in grid: "  + iter.next());
        }
    }

    public SudokuGrid(Holder<SudokuGridSettings> settings, List<BoxIndex> boxIndices) {
        this.settings = settings;
        this.boxes = new ArrayList<>(this.getGridLength() * this.getGridLength());
        boxIndices.sort(Comparator.comparingInt(value -> value.column() * this.getGridLength() + value.row()));

        Iterator<SudokuGridSettings.InitialValue> initialIter = settings.value().initialValues().iterator();
        Iterator<BoxIndex> boxIter = boxIndices.iterator();
        var initialValue = initialIter.next();
        var boxIndex = boxIter.next();
        for (int i = 0; i < this.getGridLength() * this.getGridLength(); i++) {
            // Construct initial value
            SudokuBox box;
            if (initialValue != null && initialValue.index(this.getGridLength()) == i) {
                box = new SudokuBox(initialValue.value());
                initialValue = initialIter.hasNext() ? initialIter.next() : null;
            } else {
                box = new SudokuBox();
            }
            this.boxes.add(box);

            // Check box index
            if (boxIndex != null && boxIndex.index(this.getGridLength()) == i) {
                box.mergeMarkings(boxIndex.box());
                boxIndex = boxIter.hasNext() ? boxIter.next() : null;
            }
        }

        if (initialIter.hasNext()) {
            throw new IllegalStateException("Initial value(s) was not completely set in grid: "  + initialIter.next());
        }
        if (boxIter.hasNext()) {
            throw new IllegalStateException("Box was not completely set in grid: "  + boxIter.next());
        }
    }

    public Holder<SudokuGridSettings> getSettings() {
        return this.settings;
    }

    public void applyConstraints(int rowIdx, int columnIdx, BiConsumer<Integer, Integer> constraint) {
        this.getSettings().value().constraints().forEach(constr -> constr.value().apply(this.getSettings().value(), rowIdx, columnIdx, constraint));
    }

    public int getGridLength() {
        return this.settings.value().gridLength();
    }

    public SudokuBox getBox(int rowIdx, int columnIdx) {
        return this.boxes.get(rowIdx * this.getGridLength() + columnIdx);
    }

    public List<BoxIndex> getBoxIndices() {
        List<BoxIndex> boxIndices = new ArrayList<>();

        for (int i = 0; i < this.boxes.size(); i++) {
            var box = this.boxes.get(i);
            if (box.containsData()) {
                boxIndices.add(new BoxIndex((i / this.getGridLength()) + 1, (i % this.getGridLength()) + 1, box));
            }
        }

        return boxIndices;
    }

    public record BoxIndex(int row, int column, SudokuBox box) {
        public static final Codec<BoxIndex> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("row").forGetter(SudokuGrid.BoxIndex::row),
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("column").forGetter(SudokuGrid.BoxIndex::column),
                        SudokuBox.CODEC.fieldOf("box").forGetter(SudokuGrid.BoxIndex::box)
                ).apply(instance, SudokuGrid.BoxIndex::new)
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
