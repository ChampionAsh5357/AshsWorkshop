package net.ashwork.mc.ashsworkshop.experimental.init;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ExperimentalWorkshopRegistries {

    // Custom Registries TODO: Document

    public static final ResourceKey<Registry<SudokuMarking.Type<?, ?>>> SUDOKU_MARKING_TYPE_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("sudoku_marking_type"));
    public static final Registry<SudokuMarking.Type<?, ?>> SUDOKU_MARKING_TYPE = new RegistryBuilder<>(SUDOKU_MARKING_TYPE_KEY).create();
    public static final ResourceKey<Registry<SudokuGridSettings>> SUDOKU_GRID_KEY = ResourceKey.createRegistryKey(AshsWorkshop.fromMod("sudoku_grid"));

    public static void registerRegistries(IEventBus modBus) {
        modBus.addListener(ExperimentalWorkshopRegistries::newRegistries);
        modBus.addListener(ExperimentalWorkshopRegistries::newDataPackRegistries);
    }

    private static void newRegistries(NewRegistryEvent event) {
        event.register(SUDOKU_MARKING_TYPE);
    }

    private static void newDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(SUDOKU_GRID_KEY, SudokuGridSettings.DIRECT_CODEC, SudokuGridSettings.DIRECT_CODEC);
    }
}
