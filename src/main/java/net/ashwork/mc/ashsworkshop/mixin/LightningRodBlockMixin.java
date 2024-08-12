/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.mixin;

import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.minecraft.core.BlockPos;
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

    @Inject(at = @At("TAIL"), method = "onLightningStrike")
    public void onLightningStrike(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        var poweredPos = pos.relative(state.getValue(LightningRodBlock.FACING).getOpposite());
        var powered = level.getBlockState(poweredPos);
        if (powered.getBlock() == Blocks.CRAFTING_TABLE) {
            level.setBlockAndUpdate(poweredPos, BlockRegistrar.WORKBENCH.get().defaultBlockState());
        }
    }
}
