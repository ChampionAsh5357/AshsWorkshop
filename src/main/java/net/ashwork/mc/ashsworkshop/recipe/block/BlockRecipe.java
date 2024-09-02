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

/**
 * A recipe extension that operates on blocks.
 *
 * @param <T> the type of the recipe input
 */
public interface BlockRecipe<T extends AttachedBlockInput> extends Recipe<T> {

    /**
     * Sets the state of the block within the world. Analogous to {@link #assemble(AttachedBlockInput, HolderLookup.Provider)}.
     *
     * @param input the recipe input
     * @param registries the registries within the game
     * @return the new block state to set
     */
    BlockState setState(T input, HolderLookup.Provider registries);

    /**
     * Gets the result state of this recipe. Analogous to {@link #getResultItem(HolderLookup.Provider)}.
     *
     * @param registries the registries within the game
     * @return the resulting block state of this recipe
     */
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
