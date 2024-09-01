package net.ashwork.mc.ashsworkshop.game.sudoku.analysis;

import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.analysis.AnalysisContext;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.game.sudoku.saveddata.SudokuData;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SudokuAnalysis implements Analysis<SudokuAnalysis.SudokuContext> {

    @Override
    public boolean canAnalyze(Player player, SudokuAnalysis.SudokuContext context) {
        return player instanceof ServerPlayer sp
                // Can analyze if the player cannot view
                ? !SudokuData.init(sp).canView(context.settings())
                : Analysis.super.canAnalyze(player, context);
    }

    @Override
    public void unlock(Player player, SudokuAnalysis.SudokuContext context) {
        if (player instanceof ServerPlayer sp) {
            SudokuData.init(sp).enableView(context.settings());
        }
    }

    @Override
    public ResourceLocation getAnalyzedName(SudokuAnalysis.SudokuContext context) {
        return context.settings().unwrapKey().map(ResourceKey::location)
                .map(location -> location.withPrefix("sudoku_grid_")).orElseThrow();
    }

    public record SudokuContext(Level level, Vec3 position, Holder<SudokuGridSettings> settings) implements AnalysisContext {}
}
