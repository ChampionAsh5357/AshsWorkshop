/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.grid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.SudokuBox;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * The grid the player interacts with when attempting to complete a sudoku puzzle.
 */
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
    public static final StreamCodec<RegistryFriendlyByteBuf, SudokuGrid> STREAM_CODEC = StreamCodec.composite(
            SudokuGridSettings.STREAM_CODEC, SudokuGrid::getSettings,
            BoxIndex.STREAM_CODEC.apply(ByteBufCodecs.list()), SudokuGrid::getBoxIndices,
            SudokuGrid::new
    );

    private final Holder<SudokuGridSettings> settings;
    private final List<SudokuBox> boxes;

    public SudokuGrid(Holder<SudokuGridSettings> settings) {
        settings.value().validate();

        this.settings = settings;
        this.boxes = new ArrayList<>(this.getGridLength() * this.getGridLength());

        // Iterate through all initial values and set them in the grid
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
        boxIndices = new ArrayList<>(boxIndices);
        boxIndices.sort(Comparator.comparingInt(value -> value.index(this.getGridLength())));

        // Iterate through all initial and marked boxes and set them in the grid
        Iterator<SudokuGridSettings.InitialValue> initialIter = settings.value().initialValues().iterator();
        Iterator<BoxIndex> boxIter = boxIndices.iterator();
        var initialValue = initialIter.next();
        var boxIndex = boxIter.hasNext() ? boxIter.next() : null;
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

    /**
     * Applies the constraint of the grid.
     *
     * @param rowIdx the index of the row the constraint is being applied to
     * @param columnIdx the index of the column the constraint is being applied to
     * @param constraint a (row, column) consumer that accepts the indexes that this position conflicts with
     */
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
                boxIndices.add(new BoxIndex(i / this.getGridLength(), i % this.getGridLength(), box));
            }
        }

        return boxIndices;
    }

    /**
     * {@return the solution state of the grid after it has been checked}
     */
    public SudokuGridSettings.SolutionState checkSolution() {
        return this.settings.value().hasSolution()
                ? this.checkSolutionFromHash()
                : this.checkSolutionFromConstraints();
    }

    private SudokuGridSettings.SolutionState checkSolutionFromHash() {
        StringBuilder builder = new StringBuilder(this.boxes.size());
        for (var box : this.boxes) {
            var marking = box.getMarking(MarkingRegistrar.MAIN.get());
            if (!marking.containsData()) {
                return SudokuGridSettings.SolutionState.IN_PROGRESS;
            }
            builder.append(marking.getValue());
        }

        return this.settings.value().checkSolution(builder.toString());
    }

    // Brute force approach
    private SudokuGridSettings.SolutionState checkSolutionFromConstraints() {
        MutableBoolean isUnsolved = new MutableBoolean(false);
        // Apply constraints to each box and mark on conflict
        for (int idx = 0; idx < this.getGridLength() * this.getGridLength(); idx++) {
            var value = this.boxes.get(idx).mainValue();
            if (value == null) {
                // A null value means that the puzzle is still inprogress
                return SudokuGridSettings.SolutionState.IN_PROGRESS;
            }

            int tempIdx = idx;
            this.applyConstraints(idx / this.getGridLength(), idx % this.getGridLength(), (rowIdx, columnIdx) -> {
                int conflictIdx = rowIdx * this.getGridLength() + columnIdx;
                // Make sure temp and conflict index do not match
                // And if the value of the two different boxes are the same
                if (tempIdx != conflictIdx && value == this.boxes.get(conflictIdx).mainValue()) {
                    isUnsolved.setValue(true);
                }
            });

            if (isUnsolved.booleanValue()) {
                return SudokuGridSettings.SolutionState.IN_PROGRESS;
            }
        }

        return SudokuGridSettings.SolutionState.FINISHED_NOT_VALIDATED;
    }

    public record BoxIndex(int rowIdx, int columnIdx, SudokuBox box) {
        public static final Codec<BoxIndex> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        WorkshopCodecs.indexOrValue("row").forGetter(BoxIndex::rowIdx),
                        WorkshopCodecs.indexOrValue("column").forGetter(BoxIndex::columnIdx),
                        SudokuBox.CODEC.fieldOf("box").forGetter(BoxIndex::box)
                ).apply(instance, BoxIndex::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, BoxIndex> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, BoxIndex::rowIdx,
                ByteBufCodecs.VAR_INT, BoxIndex::columnIdx,
                SudokuBox.STREAM_CODEC, BoxIndex::box,
                BoxIndex::new
        );

        public int index(int gridLength) {
            return this.rowIdx() * gridLength + this.columnIdx();
        }
    }
}
