package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface AnalysisContext {

    boolean validate();

    Level level();

    Vec3 position();

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
