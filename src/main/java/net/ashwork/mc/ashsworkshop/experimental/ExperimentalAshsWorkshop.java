/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.experimental.init.ConstraintRegistrar;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalRegistrars;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.ashwork.mc.ashsworkshop.experimental.init.SudokuGridSettingsRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * The experimental common mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(AshsWorkshop.ID)
public class ExperimentalAshsWorkshop {

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public ExperimentalAshsWorkshop(IEventBus modBus) {
        ExperimentalWorkshopRegistries.registerRegistries(modBus);
        ExperimentalRegistrars.registerRegistrars(modBus);
    }

    public static CompletableFuture<HolderLookup.Provider> getExperimentalRegistries(CompletableFuture<HolderLookup.Provider> registries, Function<DataProvider.Factory<DatapackBuiltinEntriesProvider>, DatapackBuiltinEntriesProvider> registrar) {
        return registrar.apply(output -> new DatapackBuiltinEntriesProvider(
                output, registries, new RegistrySetBuilder()
                .add(ExperimentalWorkshopRegistries.SUDOKU_CONSTRAINT_KEY, ConstraintRegistrar::bootstrap)
                .add(ExperimentalWorkshopRegistries.SUDOKU_GRID_KEY, SudokuGridSettingsRegistrar::bootstrap),
                Set.of(AshsWorkshop.ID)
        )).getRegistryProvider();
    }
}
