package net.ashwork.mc.ashsworkshop.game.sudoku.analysis;

import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.analysis.AnalysisContext;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.game.sudoku.saveddata.SudokuData;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.lang.ref.WeakReference;
import java.util.stream.Stream;

public class SudokuAnalysis implements Analysis<SudokuAnalysis.Context> {

    @Override
    public boolean canAnalyze(Context context) {
        if (context.player().get() instanceof ServerPlayer sp) {
            // Only analyze if it can't be viewed
            return !SudokuData.init(sp).canView(context.settings());
        }

        return Analysis.super.canAnalyze(context);
    }

    @Override
    public void unlock(Context context) {
        if (context.player().get() instanceof ServerPlayer sp) {
            // Set view to true
            SudokuData.init(sp).setView(context.settings(), true);
        }
    }

    @Override
    public ResourceLocation getResourceToAnalyze(Context context) {
        // Only use holders with references
        return context.settings().unwrapKey().orElseThrow().location();
    }

    @Override
    public void modifyFromCommand(ServerPlayer player, HolderLookup.Provider registries, ResourceLocation analyzed, boolean unlocking) {
        // Set view based on lock state
        SudokuData.init(player).setView(registries.holderOrThrow(ResourceKey.create(WorkshopRegistries.SUDOKU_GRID_KEY, analyzed)), unlocking);
    }

    @Override
    public Stream<ResourceLocation> allAnalyzableResources(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(WorkshopRegistries.SUDOKU_GRID_KEY).listElementIds().map(ResourceKey::location);
    }

    public record Context(WeakReference<Player> player, Holder<SudokuGridSettings> settings) implements AnalysisContext {}
}
