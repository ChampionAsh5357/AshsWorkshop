package net.ashwork.mc.ashsworkshop.experimental.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.BoxTintMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.CenterMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.CornerMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.MainMarking;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;

import java.util.function.Supplier;

public class MarkingRegistrar {

    public static final Supplier<SudokuMarking.Type<MainMarking>> MAIN = registerType("main", MainMarking::new, MainMarking.CODEC);
    public static final Supplier<SudokuMarking.Type<CenterMarkings>> CENTER = registerType("center", CenterMarkings::new, CenterMarkings.CODEC);
    public static final Supplier<SudokuMarking.Type<CornerMarkings>> CORNER = registerType("corner", CornerMarkings::new, CornerMarkings.CODEC);
    public static final Supplier<SudokuMarking.Type<BoxTintMarkings>> BOX_TINT = registerType("box_tint", BoxTintMarkings::new, BoxTintMarkings.CODEC);

    private static <T extends SudokuMarking> Supplier<SudokuMarking.Type<T>> registerType(String name, Supplier<T> factory, MapCodec<T> codec) {
        return ExperimentalRegistrars.SUDOKU_MARKING_TYPE.register(name, () -> new SudokuMarking.Type<>(factory, codec));
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
