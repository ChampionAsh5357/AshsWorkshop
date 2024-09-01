package net.ashwork.mc.ashsworkshop.analysis;

import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Indicates a thing that can be analyzed for unlockable information.
 *
 * @param <C> the context of what is being analyzed
 */
public interface Analysis<C extends AnalysisContext> {

    // Can be overridden to check a given context
    // If store in holder is true, also checks if it has been analyzed in the holder
    default boolean canAnalyze(Player player, C context) {
        return true;
    }

    // Handles any unlocking information
    default void unlock(Player player, C context) {}

    default ResourceLocation getAnalyzedName(C context) {
        return WorkshopRegistries.ANALYSIS.getKey(this);
    }
}
