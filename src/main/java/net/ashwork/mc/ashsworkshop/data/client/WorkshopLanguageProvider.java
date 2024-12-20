/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.data.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.command.AnalyzeCommand;
import net.ashwork.mc.ashsworkshop.init.ItemRegistrar;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.init.SudokuGridSettingsRegistrar;
import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.ashwork.mc.ashsworkshop.integration.util.IntegrationComponents;
import net.ashwork.mc.ashsworkshop.util.WorkshopComponents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;

/**
 * An English (United States) localization provider for the 'ashs_workshop' mod.
 */
public class WorkshopLanguageProvider extends LanguageProvider {

    /**
     * A simple constructor.
     *
     * @param output the root output location of the pack
     */
    public WorkshopLanguageProvider(PackOutput output) {
        super(output, AshsWorkshop.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(BlockRegistrar.WORKBENCH, "Ash's Workbench");
        this.addItem(ItemRegistrar.ANALYZER, "Workbench Analyzer");

        this.add(WorkshopComponents.WORKBENCH_MENU_KEY, "Ash's Workbench");
        this.add(WorkshopComponents.LIGHTNING_ROD_RECIPE_TYPE_KEY, "Lightning Rod");

        // Commands
        this.add(AnalyzeCommand.ANALYZE_FAILED_PLAYER, "The analyze command must be run for a player.");
        this.add(AnalyzeCommand.ANALYZE_ADD_FAILED_EXISTS, "Resource for analysis '%s' already unlocked: %s");
        this.add(AnalyzeCommand.ANALYZE_ADD_FAILED_UNLOCK, "Resource '%s' for analysis '%s' failed to unlock: %s");
        this.add(AnalyzeCommand.ANALYZE_ADD_SUCCESS, "Unlocking resource for analysis '%s': %s");
        this.add(AnalyzeCommand.ANALYZE_CLEAR_SUCCESS, "Cleared resources for analyses");
        this.add(AnalyzeCommand.ANALYZE_REMOVE_FAILED_NONEXISTENT, "Resource for analysis '%s' already locked: %s");
        this.add(AnalyzeCommand.ANALYZE_REMOVE_FAILED_LOCK, "Resource '%s' for analysis '%s' failed to lock: %s");
        this.add(AnalyzeCommand.ANALYZE_REMOVE_SUCCESS, "Locking resource for analysis '%s': %s");

        for (int i = 0; i < 9; i++) {
            this.add(WorkshopComponents.createWithSuffix(SudokuGridSettingsRegistrar.settings(
                    "generated/9x9/" + generatedSudokuDifficulty(i / 3) + "_" + ((i % 3) + 1)
            ), "title"), StringUtils.capitalise(generatedSudokuDifficulty(i / 3)) + " " + ((i % 3) + 1) + " (9x9)");
        }
        for (int i = 0; i < 9; i++) {
            this.add(WorkshopComponents.createWithSuffix(SudokuGridSettingsRegistrar.settings(
                    "generated/6x6/" + generatedSudokuDifficulty(i / 3) + "_" + ((i % 3) + 1)
            ), "title"), StringUtils.capitalise(generatedSudokuDifficulty(i / 3)) + " " + ((i % 3) + 1) + " (6x6)");
        }
        for (int i = 0; i < 3; i++) {
            this.add(WorkshopComponents.createWithSuffix(SudokuGridSettingsRegistrar.settings(
                    "generated/4x4/" + generatedSudokuDifficulty(0) + "_" + ((i % 3) + 1)
            ), "title"), StringUtils.capitalise(generatedSudokuDifficulty(0)) + " " + ((i % 3) + 1) + " (4x4)");
        }
        this.add(WorkshopComponents.createFromRegistryKey(WorkshopRegistries.SUDOKU_GRID_KEY, "standard.description"), "Standard sudoku rules apply.");

        // Integration
        this.add(IntegrationComponents.LIGHTNING_STRIKE_KEY, "Lightning Strike");
    }

    private static String generatedSudokuDifficulty(int idx) {
        return switch(idx) {
            case 0 -> "easy";
            case 1 -> "medium";
            case 2 -> "hard";
            default -> throw new IllegalStateException("Unexpected value: " + idx);
        };
    }
}
