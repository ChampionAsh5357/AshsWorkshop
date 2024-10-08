/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.marking.handler;

import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.SudokuMarking;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * The interaction handler for sudoku markings. Handles what happens when a marking selector is held and
 * an associated valid value is pressed.
 * TODO: Needs a rewrite to key mappings (which may not be possible in Mojang's system)
 * TODO: Needs a better way to handle as this is not that maintainable
 */
public class MarkingInteractionHandler {

    private final List<Modifier> typeToModifiers;

    public MarkingInteractionHandler() {
        this.typeToModifiers = new ArrayList<>();
    }

    public void registerModifiers(SudokuMarking.Type<?> type, int... modifiers) {
        this.typeToModifiers.add(new Modifier(type, modifiers));
        this.typeToModifiers.sort(Comparator.naturalOrder());
    }

    public boolean applyModifiers(int modifiers, Predicate<SudokuMarking.Type<?>> apply, boolean reverse) {
        for (var mod : (reverse ? this.typeToModifiers.reversed() : this.typeToModifiers)) {
            if (mod.canApply(modifiers) && apply.test(mod.type())) {
                return true;
            }
        }

        return false;
    }

    private record Modifier(SudokuMarking.Type<?> type, List<Integer> modifiers) implements Comparable<Modifier> {

        private Modifier(SudokuMarking.Type<?> type, int... modifiers) {
            this(type, IntStream.of(modifiers).boxed().toList());
        }

        public boolean canApply(int mods) {
            return this.modifiers.isEmpty() || this.modifiers.stream().allMatch(mod -> (mods & mod) != 0);
        }


        @Override
        public int compareTo(@NotNull MarkingInteractionHandler.Modifier o) {
            // Greater modifier list goes first
            return Integer.compare(this.modifiers.size(), o.modifiers().size());
        }
    }
}
