/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.BoxConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.ColumnConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.RowConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.SudokuConstraint;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.function.TriFunction;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ConstraintRegistrar {

    public static final ResourceKey<SudokuConstraint> ROW = constraint("row");
    public static final ResourceKey<SudokuConstraint> COLUMN = constraint("column");
    public static final ResourceKey<SudokuConstraint> BOX_3x3 = constraint("box_3x3");
    public static final ResourceKey<SudokuConstraint> BOX_2x3 = constraint("box_2x3");
    public static final ResourceKey<SudokuConstraint> BOX_2x2 = constraint("box_2x2");

    public static void bootstrap(BootstrapContext<SudokuConstraint> bootstrap) {
        bootstrap.register(ROW, RowConstraint.INSTANCE);
        bootstrap.register(COLUMN, ColumnConstraint.INSTANCE);
        bootstrap.register(BOX_3x3, new BoxConstraint(3));
        bootstrap.register(BOX_2x3, new BoxConstraint(2, 3));
        bootstrap.register(BOX_2x2, new BoxConstraint(2));
    }

    private static ResourceKey<SudokuConstraint> constraint(String name) {
        return WorkshopRegistrars.dataKey(WorkshopRegistries.SUDOKU_CONSTRAINT_KEY, name);
    }

    public static class Tags {

        public static final TagKey<SudokuConstraint> STANDARD_ROW_COLUMN = constraint("standard/row_column");
        public static final TagKey<SudokuConstraint> STANDARD_9x9 = constraint("standard/9x9");
        public static final TagKey<SudokuConstraint> STANDARD_6x6 = constraint("standard/6x6");
        public static final TagKey<SudokuConstraint> STANDARD_4x4 = constraint("standard/4x4");

        public static void constraintTags(Function<TriFunction<ExistingFileHelper, CompletableFuture<HolderLookup.Provider>, PackOutput, TagsProvider<SudokuConstraint>>, TagsProvider<SudokuConstraint>> factory) {
            WorkshopRegistrars.tagProvider(WorkshopRegistries.SUDOKU_CONSTRAINT_KEY, factory, (registries, tag) -> {
                tag.apply(STANDARD_ROW_COLUMN).add(ROW, COLUMN);
                tag.apply(STANDARD_9x9).addTag(STANDARD_ROW_COLUMN).add(BOX_3x3);
                tag.apply(STANDARD_6x6).addTag(STANDARD_ROW_COLUMN).add(BOX_2x3);
                tag.apply(STANDARD_4x4).addTag(STANDARD_ROW_COLUMN).add(BOX_2x2);
            });
        }

        private static TagKey<SudokuConstraint> constraint(String name) {
            return WorkshopRegistrars.tagKey(WorkshopRegistries.SUDOKU_CONSTRAINT_KEY, name);
        }
    }
}
