/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.integration.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;

public class BlockPredicateStackHelper implements IIngredientHelper<BlockPredicate> {

    @Override
    public IIngredientType<BlockPredicate> getIngredientType() {
        return WorkshopJeiPlugin.BLOCK_PREDICATE_TYPE;
    }

    @Override
    public String getDisplayName(BlockPredicate ingredient) {
        return ingredient.toString();
    }

    @Override
    public String getUniqueId(BlockPredicate ingredient, UidContext context) {
        return this.getIngredientType().getUid() + ":" + this.getResourceLocation(ingredient);
    }

    @Override
    public ResourceLocation getResourceLocation(BlockPredicate ingredient) {
        return AshsWorkshop.fromMod(ingredient.toString().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9/._-]", "_"));
    }

    @Override
    public BlockPredicate copyIngredient(BlockPredicate ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable BlockPredicate ingredient) {
        return Objects.toString(ingredient);
    }
}
