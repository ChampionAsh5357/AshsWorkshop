package net.ashwork.mc.ashsworkshop.analysis;

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

    // Stores the analyzed resources in the holder itself
    default boolean storeInHolder() {
        return true;
    }
}
