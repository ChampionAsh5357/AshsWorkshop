/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.mixin;

import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {

    @Inject(at = @At("HEAD"), method = "onLightningStrike", cancellable = true)
    public void onLightningStrike(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        var poweredDirection = state.getValue(LightningRodBlock.FACING).getOpposite();
        if (poweredDirection == Direction.DOWN) {
            var poweredPos = pos.relative(poweredDirection);
            var powered = level.getBlockState(poweredPos);
            // Lightning rod on crafting table
            if (powered.getBlock() == Blocks.CRAFTING_TABLE) {
                // Update blocks
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(poweredPos, BlockRegistrar.WORKBENCH.get().defaultBlockState());
                // Spawn particles
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.GUST_EMITTER_SMALL,
                            poweredPos.getX() + 0.5, poweredPos.getY() + 0.5, poweredPos.getZ() + 0.5, 1,
                            0.1, 0.1, 0.1, 1);
                    serverLevel.sendParticles(ParticleTypes.GUST_EMITTER_SMALL,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1,
                            0.1, 0.1, 0.1, 1);
                }
                ci.cancel();
            }
        }
    }
}
