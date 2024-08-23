package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface Analyzable {

    ResourceLocation unlock();

    interface Context {
        boolean validate();
    }

    record BlockContext(Level level, BlockPos pos, BlockState state) implements Context {

        @Override
        public boolean validate() {
            return this.level.getBlockState(this.pos) == this.state;
        }
    }
}
