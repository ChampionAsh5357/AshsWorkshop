/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.grid;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.ashwork.mc.ashsworkshop.attribution.AttributionInfo;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.SudokuConstraint;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.ashwork.mc.ashsworkshop.util.WorkshopComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A datapack object that represents a sudoku grid that can be solved.
 *
 * @param gridLength the length of one size of the square grid
 * @param initialValues the initial values within the grid, locked
 * @param constraints the constraints applied to the grid
 * @param solutionMD5 the MD5 hash of the solution, if present
 * @param attribution the attribution of the original grid, if present
 */
public record SudokuGridSettings(int gridLength, List<InitialValue> initialValues, HolderSet<SudokuConstraint> constraints, Optional<String> solutionMD5, Optional<AttributionInfo> attribution) {

    public static final Codec<SudokuGridSettings> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.intRange(4, Integer.MAX_VALUE).fieldOf("grid_length").forGetter(SudokuGridSettings::gridLength),
                    InitialValue.CODEC.listOf().fieldOf("initial_values").forGetter(SudokuGridSettings::initialValues),
                    SudokuConstraint.LIST_CODEC.fieldOf("constraints").forGetter(SudokuGridSettings::constraints),
                    Codec.STRING.optionalFieldOf("solution_md5").forGetter(SudokuGridSettings::solutionMD5),
                    AttributionInfo.CODEC.optionalFieldOf("attribution").forGetter(SudokuGridSettings::attribution)
            ).apply(instance, SudokuGridSettings::new)
    );
    public static final Codec<Holder<SudokuGridSettings>> CODEC = RegistryFileCodec.create(WorkshopRegistries.SUDOKU_GRID_KEY, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<SudokuGridSettings>> STREAM_CODEC = ByteBufCodecs.holderRegistry(WorkshopRegistries.SUDOKU_GRID_KEY);

    public SudokuGridSettings(int gridLength, List<InitialValue> initialValues, HolderSet<SudokuConstraint> constraints, Optional<String> solutionMD5, Optional<AttributionInfo> attribution) {
        this.gridLength = gridLength;
        this.constraints = constraints;
        this.solutionMD5 = solutionMD5.map(str -> str.toUpperCase(Locale.ROOT));
        this.attribution = attribution;

        // TODO: Figure out how to handle better
        var mutable = new ArrayList<>(initialValues);
        mutable.sort(Comparator.comparingInt(value -> value.index(this.gridLength)));
        this.initialValues = ImmutableList.copyOf(mutable);

        IntSet validator = new IntOpenHashSet();
        for (var value : this.initialValues) {
            if (value.rowIdx() >= this.gridLength || value.columnIdx() >= this.gridLength) {
                throw new IllegalArgumentException("Initial index (" + value.rowIdx() + ", " + value.columnIdx() + ") is not within grid " + this.gridLength);
            } else if (!validator.add(value.index(this.gridLength))) {
                throw new IllegalArgumentException("Duplicate initial value index (" + value.rowIdx() + ", " + value.columnIdx() + ")");
            }
        }
    }

    /**
     * Validates the constraints are legal.
     */
    public void validate() {
        // Once everything is set, validate all constraints
        this.constraints.stream().filter(constr -> !constr.value().validate(this)).findFirst().ifPresent(c -> {
            throw new IllegalArgumentException("One or more constraints failed on validation.");
        });
    }

    public boolean hasSolution() {
        return this.solutionMD5.isPresent();
    }

    public SolutionState checkSolution(String solution) {
        return this.solutionMD5.map(
                md5 -> md5.equals(SudokuGridSettings.getEncryptedSolution(solution))
                        ? SolutionState.COMPLETE
                        : SolutionState.IN_PROGRESS
        ).orElse(SolutionState.FINISHED_NOT_VALIDATED);
    }

    private static String getEncryptedSolution(String solution) {
        return DigestUtils.md5Hex(solution).toUpperCase(Locale.ROOT);
    }

    public record InitialValue(int rowIdx, int columnIdx, Character value) {
        public static final Codec<InitialValue> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        WorkshopCodecs.indexOrValue("row").forGetter(InitialValue::rowIdx),
                        WorkshopCodecs.indexOrValue("column").forGetter(InitialValue::columnIdx),
                        WorkshopCodecs.SUDOKU_VALUE.fieldOf("value").forGetter(InitialValue::value)
                ).apply(instance, InitialValue::new)
        );

        public int index(int gridLength) {
            return this.rowIdx * gridLength + this.columnIdx;
        }
    }

    public static Builder builder(ResourceKey<SudokuGridSettings> key, int gridLength) {
        return new Builder(key, gridLength);
    }

    public static class Builder {

        private final ResourceKey<SudokuGridSettings> key;
        private final int gridLength;
        private final List<InitialValue> initialValues;
        private String initialString;
        private HolderSet<SudokuConstraint> constraints;
        @Nullable
        private AttributionInfo attribution;
        @Nullable
        private String solutionMD5;

        private Builder(ResourceKey<SudokuGridSettings> key, int gridLength) {
            this.key = key;
            this.gridLength = gridLength;
            this.initialValues = new ArrayList<>();
        }

        /**
         * Constructs the initial values from a string, written from left-to-right, top-to-bottom
         *
         * @param values the initial values of the grid
         * @return the builder instance
         */
        public Builder initialValues(String values) {
            for (var i = 0; i < values.length(); i++) {
                var val = values.charAt(i);
                if (val != ' ') {
                    this.initialValues.add(new InitialValue(i / this.gridLength, i % this.gridLength, val));
                }
            }
            this.initialString = values;
            return this;
        }

        public Builder constraints(HolderSet<SudokuConstraint> constraints) {
            this.constraints = constraints;
            return this;
        }

        public Builder attribution(String author) {
            this.attribution = new AttributionInfo(
                    Component.translatable(WorkshopComponents.createWithSuffix(this.key, "title")),
                    author,
                    Optional.of(Component.translatable(WorkshopComponents.createFromRegistryKey(WorkshopRegistries.SUDOKU_GRID_KEY, "standard.description"))),
                    Optional.empty(),
                    Optional.empty()
            );
            return this;
        }

        /**
         * Constructs the solution from a string and generates the hash, written from left-to-right, top-to-bottom
         *
         * @param solution the solution of the grid
         * @return the builder instance
         */
        public Builder solution(String solution) {
            if (this.initialString != null) {
                for (var i = 0; i < this.initialString.length(); i++) {
                    var val = this.initialString.charAt(i);
                    if (val != ' ' && val != solution.charAt(i)) {
                        throw new IllegalArgumentException("Solution character '" + solution.charAt(i) + "' at index " + i + "does not match initial value '" + val + "'.");
                    }
                }
            }

            return this.solutionMD5(SudokuGridSettings.getEncryptedSolution(solution));
        }

        public Builder solutionMD5(String solutionMD5) {
            this.solutionMD5 = solutionMD5;
            return this;
        }

        public SudokuGridSettings build() {
            return new SudokuGridSettings(this.gridLength, this.initialValues, this.constraints, Optional.ofNullable(this.solutionMD5), Optional.ofNullable(this.attribution));
        }
    }

    public enum SolutionState {
        NEW("New", false),
        IN_PROGRESS("In Progress", false),
        FINISHED_NOT_VALIDATED("Finished", true),
        COMPLETE("Complete", true);

        public static final Codec<SolutionState> CODEC = Codec.INT.xmap(id -> SolutionState.values()[id], SolutionState::ordinal);
        public static final StreamCodec<ByteBuf, SolutionState> STREAM_CODEC = ByteBufCodecs.idMapper(id -> SolutionState.values()[id], SolutionState::ordinal);

        private final String displayName;
        private final boolean isComplete;

        SolutionState(String displayName, boolean isComplete) {
            this.displayName = displayName;
            this.isComplete = isComplete;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        /**
         * {@return whether there is server data for the grid}
         */
        public boolean getServerData() {
            return this != NEW;
        }

        /**
         * {@return whether the state is considered 'complete' to the best of our knowledge}
         */
        public boolean isComplete() {
            return this.isComplete;
        }
    }
}
