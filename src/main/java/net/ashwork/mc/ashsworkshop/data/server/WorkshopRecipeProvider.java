package net.ashwork.mc.ashsworkshop.data.server;

import net.ashwork.mc.ashsworkshop.data.server.recipe.LightningRodRecipeBuilder;
import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.block.Blocks;

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
    }
}
