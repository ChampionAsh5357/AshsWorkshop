/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.recipe.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockRecipe<T extends AttachedBlockInput> extends Recipe<T> {

    BlockState setState(T input, HolderLookup.Provider registries);

    BlockState getResultState(HolderLookup.Provider registries);

    @Override
    default ItemStack assemble(T input, HolderLookup.Provider registries) {
        throw new UnsupportedOperationException("ItemStack methods are not supported on Block recipes.");
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    default NonNullList<Ingredient> getIngredients() {
        throw new UnsupportedOperationException("Ingredient methods are not supported on Block recipes.");
    }

    @Override
    default ItemStack getResultItem(HolderLookup.Provider registries) {
        throw new UnsupportedOperationException("ItemStack methods are not supported on Block recipes.");
    }

    @Override
    default NonNullList<ItemStack> getRemainingItems(T input) {
        throw new UnsupportedOperationException("ItemStack methods are not supported on Block recipes.");
    }

    @Override
    default boolean isIncomplete() {
        return false;
    }
}
