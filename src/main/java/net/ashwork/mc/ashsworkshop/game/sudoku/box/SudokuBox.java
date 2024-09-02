/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.box;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A box within the sudoku grid. Contains any marking data, along with if the box can be edited.
 */
public class SudokuBox {

    @SuppressWarnings("unchecked")
    public static final Codec<SudokuBox> CODEC = WorkshopRegistries.SUDOKU_MARKING_TYPE.byNameCodec()
            .dispatch(SudokuMarking::type, type -> (MapCodec<SudokuMarking>) type.codec())
            .listOf().xmap(SudokuBox::new, SudokuBox::getMarkings);
    @SuppressWarnings("unchecked")
    public static final StreamCodec<RegistryFriendlyByteBuf, SudokuBox> STREAM_CODEC = ByteBufCodecs.registry(
            WorkshopRegistries.SUDOKU_MARKING_TYPE_KEY
    ).dispatch(SudokuMarking::type, type -> (StreamCodec<? super RegistryFriendlyByteBuf, SudokuMarking>) type.streamCodec())
            .apply(ByteBufCodecs.list()).map(SudokuBox::new, SudokuBox::getMarkings);

    private final Map<SudokuMarking.Type<?>, SudokuMarking> markings;
    private final boolean locked;

    public SudokuBox() {
        this((Character) null);
    }

    // Constructor for creating locked boxes
    public SudokuBox(@Nullable Character initialValue) {
        this.markings = new HashMap<>();
        this.locked = initialValue != null;

        if (initialValue != null) {
           this.mark(MarkingRegistrar.MAIN.get(), initialValue);
        }
    }

    public SudokuBox(List<SudokuMarking> markings) {
        this.markings = markings.stream().collect(Collectors.toMap(SudokuMarking::type, Function.identity()));
        this.locked = false;
    }

    private List<SudokuMarking> getMarkings() {
        // Make sure the marking isn't locked or that the marking is locked but does not contain the main marking
        return this.markings.values().stream().filter(marking -> !this.isLocked() || marking.type() != MarkingRegistrar.MAIN.get())
                .filter(SudokuMarking::containsData).collect(Collectors.toList());
    }

    /**
     * {@return the main value of the box}
     */
    @Nullable
    public Character mainValue() {
        var marking = this.getMarking(MarkingRegistrar.MAIN.get());
        return marking.getValue();
    }

    /**
     * Marks the grid with the associated input. May unset data with the marked value is the same as that provided.
     *
     * @param type the marking type
     * @param value the value being marked
     * @param <T> the type of the marking
     */
    public <T extends SudokuMarking> void mark(SudokuMarking.Type<T> type, char value) {
        this.getMarking(type).mark(value);
    }

    /**
     * Clears the marked data.
     *
     * @param type the marking type
     * @return {@code true} if there was data to clear, {@code false} otherwise
     * @param <T> the type of the marking
     */
    public <T extends SudokuMarking> boolean clear(SudokuMarking.Type<T> type) {
        return this.getMarking(type).clear();
    }

    /**
     * Clears all marking data.
     */
    public void clearAll() {
        this.markings.values().forEach(SudokuMarking::clear);
    }

    /**
     * Gets the marking object of the box.
     *
     * @param type the marking type
     * @return the marking object
     * @param <T> the type of the marking
     */
    @SuppressWarnings("unchecked")
    public <T extends SudokuMarking> T getMarking(SudokuMarking.Type<T> type) {
        return (T) markings.computeIfAbsent(type, t -> t.factory().get());
    }

    /**
     * {@return {@code true} if there was data within the box from the player, {@code false} otherwise}
     */
    public boolean containsData() {
        return this.markings.values().stream().filter(marking -> !this.isLocked() || marking.type() != MarkingRegistrar.MAIN.get()).anyMatch(SudokuMarking::containsData);
    }

    /**
     * {@return {@code true} if the box is not editable, {@code false} otherwise}
     */
    public boolean isLocked() {
        return this.locked;
    }

    /**
     * Merges the markings from another box.
     *
     * @param box the box to merge the markings from
     */
    public void mergeMarkings(SudokuBox box) {
        box.markings.forEach(this.markings::putIfAbsent);
    }

    @Override
    public String toString() {
        return "SudokuBox" + this.markings.entrySet();
    }
}
