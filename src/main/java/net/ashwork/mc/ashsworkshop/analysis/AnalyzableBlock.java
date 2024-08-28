package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An interface to apply to a block that can be analyzed.
 */
public interface AnalyzableBlock {

    /**
     * Attempts to begin analyzing the block.
     *
     * @param state the state of the block being analyzed
     * @param context the context the block is analyzed under
     * @param holder the holder performing the analysis
     * @return whether the block can be analyzed
     */
    boolean analyze(BlockState state, UseOnContext context, AnalysisHolder holder);
}
