package net.ashwork.mc.ashsworkshop.experimental.init;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ExperimentalRegistrars {

    /**
     * The sudoku marking type registrar.
     */
    static final DeferredRegister<SudokuMarking.Type<?, ?>> SUDOKU_MARKING_TYPE = DeferredRegister.create(ExperimentalWorkshopRegistries.SUDOKU_MARKING_TYPE, AshsWorkshop.ID);

    public static void registerRegistrars(IEventBus modBus) {
        // Add registrars
        SUDOKU_MARKING_TYPE.register(modBus);

        // Load registry classes
        MarkingRegistrar.register();
    }
}
