package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public interface AnalyzableBlock {

    boolean analyze(BlockState state, UseOnContext context, AnalysisHolder holder);
}
