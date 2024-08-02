package net.ashwork.mc.ashsworkshop.experimental.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.CenterMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.CornerMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.MainMarking;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.SudokuMarking;
import net.minecraft.core.Holder;

public class MarkingRegistrarAlpha1 {

    public static final Holder<SudokuMarking.Type> MAIN = registerType("main", MainMarking.CODEC);
    public static final Holder<SudokuMarking.Type> CORNERS = registerType("corners", CornerMarkings.CODEC);
    public static final Holder<SudokuMarking.Type> CENTERS = registerType("centers", CenterMarkings.CODEC);

    private static Holder<SudokuMarking.Type> registerType(String name, MapCodec<? extends SudokuMarking> codec) {
        return ExperimentalRegistrars.SUDOKU_MARKING_TYPE_ALPHA_1.register(name, () -> new SudokuMarking.Type(codec));
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
