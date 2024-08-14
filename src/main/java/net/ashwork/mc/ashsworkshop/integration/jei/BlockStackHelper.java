package net.ashwork.mc.ashsworkshop.integration.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class BlockStackHelper implements IIngredientHelper<Block> {

    @Override
    public IIngredientType<Block> getIngredientType() {
        return WorkshopJeiPlugin.BLOCK_TYPE;
    }

    @Override
    public String getDisplayName(Block ingredient) {
        return ingredient.getName().getString();
    }

    @Override
    public String getUniqueId(Block ingredient, UidContext context) {
        return this.getIngredientType().getUid() + ":" + this.getResourceLocation(ingredient);
    }

    @Override
    public ResourceLocation getResourceLocation(Block ingredient) {
        return BuiltInRegistries.BLOCK.getKey(ingredient);
    }

    @Override
    public Block copyIngredient(Block ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable Block ingredient) {
        if (ingredient == null) {
            ingredient = Blocks.AIR;
        }
        return this.getResourceLocation(ingredient).toString();
    }
}
