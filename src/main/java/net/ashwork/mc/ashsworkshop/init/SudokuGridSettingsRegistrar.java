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
                .initialValues("1  6  4   7 2   1548 9 5    3  2 97 8  531  6 61 9  8    3 7 2474   2 9   8  9  7")
                .solution("152678439679243815483915762534826971897531246261794583915387624746152398328469157"));
        registerGenerated(bootstrap, "generated/9x9/easy_2", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("54  2 9 3     9  292 5  8    4  76  176   235  96  4    2  5 643  7     4 5 6  97")
                .solution("547826913618439752923571846254317689176948235839652471792185364361794528485263197"));
        registerGenerated(bootstrap, "generated/9x9/easy_3", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues(" 6491 5 3  127 4   2      7    2 1  5 8 3 2 9  2 6    8      5   9 576  1 7 8694 ")
                .solution("764918523381275496925643817673529184518734269492861375836492751249157638157386942"));
        registerGenerated(bootstrap, "generated/9x9/medium_1", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("   4 5 76 4 7 93    82 3  9       3  15 7 84  6       1  3 72    21 8 6 79 6 4   ")
                .solution("239485176541769328678213459927841635315976842864532917186357294452198763793624581"));
        registerGenerated(bootstrap, "generated/9x9/medium_2", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("52  7    3  1    6  1 2   518    762   2 1   243    914   5 1  6    2  3    1  54")
                .solution("524673819397185246861429375189534762756291438243867591438956127615742983972318654"));
        registerGenerated(bootstrap, "generated/9x9/medium_3", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("    4 27  265 7  4  4     561  9   7 8 1 4 2 9   6  382     3  4  8 279  59 1    ")
                .solution("895641273326587914174923865613298547587134629942765138268479351431852796759316482"));
        registerGenerated(bootstrap, "generated/9x9/hard_1", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues(" 2       4   68  3   9 72 8  65   1 3  8 9  5 1   67  5 14 3   8  69   1       9 ")
                .solution("728134956459268173163957248986572314347819625215346789591423867872695431634781592"));
        registerGenerated(bootstrap, "generated/9x9/hard_2", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("     162 2 48 6    1      93  6   5   1   4   6   3  78      9    2 73 1 759     ")
                .solution("739541628254896173618372549347619852981725436562483917823164795496257381175938264"));
        registerGenerated(bootstrap, "generated/9x9/hard_3", 9, builder -> builder.constraints(standard9x9Constraints)
                .initialValues("98 4  6         5    2 5  43 71 4   8 9   4 5   5 92 36  8 7    3         4  3 86")
                .solution("985431627462798351713265894357124968829376415146589273691847532538612749274953186"));

        registerGenerated(bootstrap, "generated/6x6/easy_1", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("5   43  4 626  254   63  2631 351   ").solution("562143134562613254245631426315351426"));
        registerGenerated(bootstrap, "generated/6x6/easy_2", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("      5 32  1256 3 361523 2 64 54   ").solution("261435543216125643436152312564654321"));
        registerGenerated(bootstrap, "generated/6x6/easy_3", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("5  3      5 162 43345261 516   3 4  ").solution("514326623154162543345261451632236415"));
        registerGenerated(bootstrap, "generated/6x6/medium_1", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues(" 421  56     5 4  4    62 56   1 2  ").solution("342165561324156432423516235641614253"));
        registerGenerated(bootstrap, "generated/6x6/medium_2", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("  2   45  1    63    1  345  162 5  ").solution("132456456312214635563124345261621543"));
        registerGenerated(bootstrap, "generated/6x6/medium_3", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues(" 51   3         6 6 4   523 16  6 32").solution("451623362145215364634251523416146532"));
        registerGenerated(bootstrap, "generated/6x6/hard_1", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("3            4 1   5 3  21  5      4").solution("325461461532643125152346214653536214"));
        registerGenerated(bootstrap, "generated/6x6/hard_2", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("  3 65 1    4        3    6  4     1").solution("243165615423432516561342156234324651"));
        registerGenerated(bootstrap, "generated/6x6/hard_3", 6, builder -> builder.constraints(standard6x6Constraints)
                .initialValues("  1  3    4  4      3 1     2 5   31").solution("461253235146142365653412316524524631"));

        registerGenerated(bootstrap, "generated/4x4/easy_1", 4, builder -> builder.constraints(standard4x4Constraints)
                .initialValues("2 4          2 3").solution("2341143231244213"));
        registerGenerated(bootstrap, "generated/4x4/easy_2", 4, builder -> builder.constraints(standard4x4Constraints)
                .initialValues("     2 11 3     ").solution("4123324114322314"));
        registerGenerated(bootstrap, "generated/4x4/easy_3", 4, builder -> builder.constraints(standard4x4Constraints)
                .initialValues("4      12      3").solution("4132324123141423"));
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
