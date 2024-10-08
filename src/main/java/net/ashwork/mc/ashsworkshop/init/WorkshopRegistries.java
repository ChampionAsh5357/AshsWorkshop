/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.attribution.AttributionInfo;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.SudokuConstraint;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class WorkshopRegistries {

    // Custom Registries TODO: Document

    public static final ResourceKey<Registry<Analysis<?>>> ANALYSIS_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("analysis"));
    public static final Registry<Analysis<?>> ANALYSIS = new RegistryBuilder<>(ANALYSIS_KEY).sync(true).create();

    public static final ResourceKey<Registry<SudokuMarking.Type<?>>> SUDOKU_MARKING_TYPE_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("sudoku_marking_type"));
    public static final Registry<SudokuMarking.Type<?>> SUDOKU_MARKING_TYPE = new RegistryBuilder<>(SUDOKU_MARKING_TYPE_KEY).sync(true).create();
    public static final ResourceKey<Registry<SudokuConstraint.Type<?>>> SUDOKU_CONSTRAINT_TYPE_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("sudoku_constraint_type"));
    public static final Registry<SudokuConstraint.Type<?>> SUDOKU_CONSTRAINT_TYPE = new RegistryBuilder<>(SUDOKU_CONSTRAINT_TYPE_KEY).sync(true).create();
    public static final ResourceKey<Registry<SudokuConstraint>> SUDOKU_CONSTRAINT_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("sudoku_constraint"));
    public static final ResourceKey<Registry<AttributionInfo.License>> LICENSE_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("license"));
    public static final ResourceKey<Registry<SudokuGridSettings>> SUDOKU_GRID_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("sudoku_grid"));

    public static void registerRegistries(IEventBus modBus) {
        modBus.addListener(WorkshopRegistries::newRegistries);
        modBus.addListener(WorkshopRegistries::newDataPackRegistries);
    }

    private static void newRegistries(NewRegistryEvent event) {
        event.register(ANALYSIS);
        event.register(SUDOKU_MARKING_TYPE);
        event.register(SUDOKU_CONSTRAINT_TYPE);
    }

    private static void newDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(SUDOKU_CONSTRAINT_KEY, SudokuConstraint.DIRECT_CODEC, SudokuConstraint.DIRECT_CODEC);
        event.dataPackRegistry(SUDOKU_GRID_KEY, SudokuGridSettings.DIRECT_CODEC, SudokuGridSettings.DIRECT_CODEC);
        event.dataPackRegistry(LICENSE_KEY, AttributionInfo.License.DIRECT_CODEC, AttributionInfo.License.DIRECT_CODEC);
    }
}
