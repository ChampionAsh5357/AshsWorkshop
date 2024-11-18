/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.data.server;

import net.ashwork.mc.ashsworkshop.data.server.recipe.LightningRodRecipeBuilder;
import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.ashwork.mc.ashsworkshop.init.ItemRegistrar;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class WorkshopRecipeProvider extends RecipeProvider {

    public WorkshopRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output, HolderLookup.Provider registries) {
        LightningRodRecipeBuilder.rod(BlockRegistrar.WORKBENCH.get(), Blocks.CRAFTING_TABLE)
                .attachedFace(Direction.UP)
                .group("workbench").save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistrar.ANALYZER.get())
                .pattern("LRL")
                .pattern("ICI")
                .pattern(" C ")
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Tags.Items.INGOTS_COPPER)
                // Recipe Triggers by view workbench screen
                .unlockedBy("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                .group("analyzer").save(output);
    }
}
