/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.integration.jei.lightningrod;

import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.ashwork.mc.ashsworkshop.recipe.LightningRodRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record LightningRodRecipeView(LightningRodRecipe recipe, Optional<Ingredient> input, Optional<ItemStack> output) implements Recipe<RecipeInput> {

    public LightningRodRecipeView(LightningRodRecipe recipe) {
        this(
                recipe,
                Optional.of(Ingredient.of(
                        recipe.blocks().stream()
                                .map(Holder::value)
                                .map(ItemStack::new)
                )).map(in -> in.isEmpty() ? null : in),
                Optional.of(
                        new ItemStack(recipe.output())
                ).map(out -> out.isEmpty() ? null : out)
        );
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return this.input.map(in -> in.test(input.getItem(0))).orElse(false);
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return this.getResultItem(registries).copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        this.input.ifPresent(ingredients::add);
        return ingredients;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.output.orElse(ItemStack.EMPTY);
    }

    @Override
    public String getGroup() {
        return this.recipe.getGroup();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        throw new UnsupportedOperationException("This is a view, serialization is not supported.");
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistrar.LIGHTNING_ROD_TYPE.get();
    }
}
