package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.analysis.SimpleAnalysis;

import java.util.function.Supplier;

/**
 * The registrar for analyses.
 */
public class AnalysisRegistrar {

    public static final Supplier<SimpleAnalysis> WORKBENCH = simpleAnalysis("workbench");

    private static Supplier<SimpleAnalysis> simpleAnalysis(String name) {
        return WorkshopRegistrars.ANALYSIS.register(name, SimpleAnalysis::new);
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
