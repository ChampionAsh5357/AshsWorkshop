/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.data;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.data.client.WorkshopBlockStateProvider;
import net.ashwork.mc.ashsworkshop.data.client.WorkshopLanguageProvider;
import net.ashwork.mc.ashsworkshop.data.server.WorkshopBlockLootSubProvider;
import net.ashwork.mc.ashsworkshop.init.ConstraintRegistrar;
import net.ashwork.mc.ashsworkshop.init.SudokuGridSettingsRegistrar;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The data mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(AshsWorkshop.ID)
public class AshsWorkshopData {

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public AshsWorkshopData(IEventBus modBus) {
        modBus.addListener(this::gatherData);
    }

    /**
     * Adds data providers to the data generator.
     *
     * @param event the event instance
     */
    private void gatherData(GatherDataEvent event) {
        // Set variables
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var registries = addProvider(generator, event.includeServer(), output -> new DatapackBuiltinEntriesProvider(
                output, event.getLookupProvider(), new RegistrySetBuilder()
                .add(WorkshopRegistries.SUDOKU_CONSTRAINT_KEY, ConstraintRegistrar::bootstrap)
                .add(WorkshopRegistries.SUDOKU_GRID_KEY, SudokuGridSettingsRegistrar::bootstrap),
                Set.of(AshsWorkshop.ID)
        )).getRegistryProvider();

        // Client providers
        addProvider(generator, event.includeClient(), WorkshopLanguageProvider::new);
        addProvider(generator, event.includeClient(), output -> new WorkshopBlockStateProvider(output, existingFileHelper));

        // Server providers
        addProvider(generator, event.includeServer(), output -> new LootTableProvider(
                output,
                Collections.emptySet(),
                List.of(
                        new LootTableProvider.SubProviderEntry(WorkshopBlockLootSubProvider::new, LootContextParamSets.BLOCK)
                ),
                registries
        ));
        ConstraintRegistrar.Tags.constraintTags(factory -> addProvider(generator, event.includeServer(), output -> factory.apply(existingFileHelper, registries, output)));
    }

    /**
     * Adds a data provider to the generator using the factory method.
     *
     * @param generator the generator to add the provider to
     * @param run whether the provider should run
     * @param factory the factory to construct a {@link DataProvider} from a {@link net.minecraft.data.PackOutput}
     * @return the constructed data provider
     * @param <T> the type of the data provider
     */
    private static <T extends DataProvider> T addProvider(DataGenerator generator, boolean run, DataProvider.Factory<T> factory) {
        return generator.addProvider(run, factory);
    }
}
