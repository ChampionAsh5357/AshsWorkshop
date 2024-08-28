package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.world.entity.player.Player;

public interface Analysis<C extends AnalysisContext> {

    // Can be overridden to check a given context
    // If store in holder is true, also checks if it has been analyzed in the holder
    default boolean canAnalyze(Player player, C context) {
        return true;
    }

    default void unlock(Player player, C context) {}

    // Stores the analyzed resources in the holder itself
    default boolean storeInHolder() {
        return true;
    }
}
