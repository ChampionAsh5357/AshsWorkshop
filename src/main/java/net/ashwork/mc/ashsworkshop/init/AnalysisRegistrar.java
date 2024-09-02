package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.analysis.AnalysisContext;
import net.ashwork.mc.ashsworkshop.analysis.BlockAnalysis;
import net.ashwork.mc.ashsworkshop.game.sudoku.analysis.SudokuAnalysis;

import java.util.function.Supplier;

/**
 * The registrar for analyses.
 */
public class AnalysisRegistrar {

    public static final Supplier<BlockAnalysis> BLOCK = analysis("block", BlockAnalysis::new);
    public static final Supplier<SudokuAnalysis> SUDOKU = analysis("sudoku", SudokuAnalysis::new);

    private static <C extends AnalysisContext, A extends Analysis<C>> Supplier<A> analysis(String name, Supplier<A> analysis) {
        return WorkshopRegistrars.ANALYSIS.register(name, analysis);
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
