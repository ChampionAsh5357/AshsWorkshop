package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * The context of the analysis.
 */
public interface AnalysisContext {

    /**
     * {@return true when the context has remained the same on final usage}
     */
    boolean validate();

    /**
     * {@return the current level}
     */
    Level level();

    /**
     * {@return the position of the element being analyzed}
     */
    Vec3 position();

    /**
     * Context for a block position.
     *
     * @param level the level the block is in
     * @param pos the position of the block being analyzed
     * @param state the original state of the block being analyzed
     */
    record BlockContext(Level level, BlockPos pos, BlockState state) implements AnalysisContext {

        @Override
        public boolean validate() {
            return this.level.getBlockState(pos) == this.state;
        }

        @Override
        public Vec3 position() {
            return this.pos.getCenter();
        }
    }
}
