/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.data.server.recipe;

import net.ashwork.mc.ashsworkshop.recipe.LightningRodRecipe;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LightningRodRecipeBuilder {

    private final HolderSet<Block> input;
    private Optional<StatePropertiesPredicate> properties;
    @Nullable
    private Direction attachedFace;
    private final Block output;
    private String group;
    private final List<ICondition> conditions;

    private LightningRodRecipeBuilder(final HolderSet<Block> input, final Block output) {
        this.input = input;
        this.output = output;
        this.group = "";
        this.properties = Optional.empty();
        this.conditions = new ArrayList<>();
    }

    public static LightningRodRecipeBuilder rod(Block output, Block... inputs) {
        return rod(output, HolderSet.direct(
                Arrays.stream(inputs).map(BuiltInRegistries.BLOCK::wrapAsHolder).toList()
        ));
    }

    public static LightningRodRecipeBuilder rod(Block output, HolderSet<Block> input) {
        return new LightningRodRecipeBuilder(input, output);
    }

    public LightningRodRecipeBuilder withProperties(StatePropertiesPredicate.Builder properties) {
        this.properties = properties.build();
        return this;
    }

    public LightningRodRecipeBuilder attachedFace(Direction attachedFace) {
        this.attachedFace = attachedFace;
        return this;
    }

    public LightningRodRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    public LightningRodRecipeBuilder withConditions(ICondition... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public void save(RecipeOutput output) {
        this.save(output, getDefaultRecipeId(this.output));
    }

    private static ResourceLocation getDefaultRecipeId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        if (this.input.size() == 0) {
            throw new IllegalStateException("No way of using recipe " + id);
        }
        var recipe = new LightningRodRecipe(
                this.group,
                new BlockPredicate(
                        Optional.of(this.input),
                        this.properties,
                        Optional.empty()
                ),
                Optional.ofNullable(this.attachedFace),
                this.output
        );
        output.accept(id, recipe, null, this.conditions.toArray(new ICondition[0]));
    }
}
