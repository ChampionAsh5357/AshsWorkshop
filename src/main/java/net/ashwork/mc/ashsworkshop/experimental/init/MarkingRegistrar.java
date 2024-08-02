package net.ashwork.mc.ashsworkshop.experimental.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.BoxTintMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.CenterMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.CornerMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.MainMarking;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking.SudokuMarking;

import java.util.function.Supplier;

public class MarkingRegistrar {

    public static final Supplier<SudokuMarking.Type<Character, MainMarking>> MAIN = registerType("main", MainMarking::new, MainMarking.CODEC);
    public static final Supplier<SudokuMarking.Type<Character, CenterMarkings>> CENTER = registerType("center", CenterMarkings::new, CenterMarkings.CODEC);
    public static final Supplier<SudokuMarking.Type<Character, CornerMarkings>> CORNER = registerType("corner", CornerMarkings::new, CornerMarkings.CODEC);
    public static final Supplier<SudokuMarking.Type<Integer, BoxTintMarkings>> BOX_TINT = registerType("box_tint", BoxTintMarkings::new, BoxTintMarkings.CODEC);

    private static <T, M extends SudokuMarking<T>> Supplier<SudokuMarking.Type<T, M>> registerType(String name, Supplier<M> factory, MapCodec<M> codec) {
        return ExperimentalRegistrars.SUDOKU_MARKING_TYPE.register(name, () -> new SudokuMarking.Type<>(factory, codec));
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
