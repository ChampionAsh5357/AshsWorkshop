/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.function.UnaryOperator;

public class SudokuGridSettingsRegistrar {

    public static void bootstrap(BootstrapContext<SudokuGridSettings> bootstrap) {
        var constraint = bootstrap.lookup(WorkshopRegistries.SUDOKU_CONSTRAINT_KEY);
        var standard9x9Constraints = constraint.getOrThrow(ConstraintRegistrar.Tags.STANDARD_9x9);
        var standard6x6Constraints = constraint.getOrThrow(ConstraintRegistrar.Tags.STANDARD_6x6);
        var standard4x4Constraints = constraint.getOrThrow(ConstraintRegistrar.Tags.STANDARD_4x4);

        registerGenerated(bootstrap, "generated/9x9/easy_1", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("1  6  4   7 2   1548 9 5    3  2 97 8  531  6 61 9  8    3 7 2474   2 9   8  9  7"));
        registerGenerated(bootstrap, "generated/9x9/easy_2", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("54  2 9 3     9  292 5  8    4  76  176   235  96  4    2  5 643  7     4 5 6  97"));
        registerGenerated(bootstrap, "generated/9x9/easy_3", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues(" 6491 5 3  127 4   2      7    2 1  5 8 3 2 9  2 6    8      5   9 576  1 7 8694 "));
        registerGenerated(bootstrap, "generated/9x9/medium_1", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("   4 5 76 4 7 93    82 3  9       3  15 7 84  6       1  3 72    21 8 6 79 6 4   "));
        registerGenerated(bootstrap, "generated/9x9/medium_2", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("52  7    3  1    6  1 2   518    762   2 1   243    914   5 1  6    2  3    1  54"));
        registerGenerated(bootstrap, "generated/9x9/medium_3", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("    4 27  265 7  4  4     561  9   7 8 1 4 2 9   6  382     3  4  8 279  59 1    "));
        registerGenerated(bootstrap, "generated/9x9/hard_1", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues(" 2       4   68  3   9 72 8  65   1 3  8 9  5 1   67  5 14 3   8  69   1       9 "));
        registerGenerated(bootstrap, "generated/9x9/hard_2", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("     162 2 48 6    1      93  6   5   1   4   6   3  78      9    2 73 1 759     "));
        registerGenerated(bootstrap, "generated/9x9/hard_3", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("98 4  6         5    2 5  43 71 4   8 9   4 5   5 92 36  8 7    3         4  3 86"));

        registerGenerated(bootstrap, "generated/6x6/easy_1", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("5   43  4 626  254   63  2631 351   "));
        registerGenerated(bootstrap, "generated/6x6/easy_2", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("      5 32  1256 3 361523 2 64 54   "));
        registerGenerated(bootstrap, "generated/6x6/easy_3", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("5  3      5 162 43345261 516   3 4  "));
        registerGenerated(bootstrap, "generated/6x6/medium_1", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues(" 421  56     5 4  4    62 56   1 2  "));
        registerGenerated(bootstrap, "generated/6x6/medium_2", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("  2   45  1    63    1  345  162 5  "));
        registerGenerated(bootstrap, "generated/6x6/medium_3", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues(" 51   3         6 6 4   523 16  6 32"));
        registerGenerated(bootstrap, "generated/6x6/hard_1", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("3            4 1   5 3  21  5      4"));
        registerGenerated(bootstrap, "generated/6x6/hard_2", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("  3 65 1    4        3    6  4     1"));
        registerGenerated(bootstrap, "generated/6x6/hard_3", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("  1  3    4  4      3 1     2 5   31"));

        registerGenerated(bootstrap, "generated/4x4/easy_1", 4, builder -> builder.constraints(standard4x4Constraints)
                .initialValues("2 4          2 3"));
        registerGenerated(bootstrap, "generated/4x4/easy_2", 4, builder -> builder.constraints(standard4x4Constraints)
                .initialValues("     2 11 3     "));
        registerGenerated(bootstrap, "generated/4x4/easy_3", 4, builder -> builder.constraints(standard4x4Constraints)
                .initialValues("4      12      3"));
    }

    private static void registerGenerated(BootstrapContext<SudokuGridSettings> bootstrap, String name, int gridLength, UnaryOperator<SudokuGridSettings.Builder> settings) {
        var registryKey = settings(name);
        var builder = SudokuGridSettings.builder(registryKey, gridLength).attribution("Generated");
        bootstrap.register(registryKey, settings.apply(builder).build());
    }

    public static ResourceKey<SudokuGridSettings> settings(String name) {
        return WorkshopRegistrars.dataKey(WorkshopRegistries.SUDOKU_GRID_KEY, name);
    }
}
