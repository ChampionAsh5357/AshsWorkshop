/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.recipe.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public record AttachedBlockInput(BlockInWorld inWorld, Direction attachedFace) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        throw new UnsupportedOperationException("ItemStack methods are not supported on Block recipes.");
    }

    @Override
    public boolean isEmpty() {
        return this.inWorld().getState().isEmpty();
    }

    @Override
    public int size() {
        return 1;
    }
}
