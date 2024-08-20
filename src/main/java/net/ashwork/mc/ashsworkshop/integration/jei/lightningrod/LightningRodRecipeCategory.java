/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.integration.jei.lightningrod;

import com.google.common.collect.ImmutableMap;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.ashwork.mc.ashsworkshop.integration.jei.WorkshopJeiPlugin;
import net.ashwork.mc.ashsworkshop.integration.util.IntegrationComponents;
import net.ashwork.mc.ashsworkshop.recipe.LightningRodRecipe;
import net.ashwork.mc.ashsworkshop.util.WorkshopComponents;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LightningRodRecipeCategory implements IRecipeCategory<LightningRodRecipeView> {

    public static final RecipeType<LightningRodRecipeView> TYPE = RecipeType.create(
            AshsWorkshop.ID,
            RecipeRegistrar.LIGHTNING_ROD_TYPE.getId().getPath(),
            LightningRodRecipeView.class
    );
    private static final Map<Direction, int[]> DIRECTION_TO_LOCATION_MAP = ImmutableMap.of(
            Direction.UP,       new int[]{10, 24, 10, 3},
            Direction.DOWN,     new int[]{10, 3, 10, 24},
            Direction.NORTH,    new int[]{0, 24, 18, 3},
            Direction.SOUTH,    new int[]{18, 3, 0, 24},
            Direction.EAST,     new int[]{0, 14, 18, 14},
            Direction.WEST,     new int[]{18, 14, 0, 14}
    );
    private static final ResourceLocation SPRITE = AshsWorkshop.fromMod("integration/jei/lightning_rod");
    private final IGuiHelper guiHelper;

    public LightningRodRecipeCategory(IGuiHelper helper) {
        this.guiHelper = helper;
    }

    @Override
    public RecipeType<LightningRodRecipeView> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return WorkshopComponents.LIGHTNING_ROD_RECIPE_TYPE;
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(90, 42);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableItemStack(new ItemStack(Items.LIGHTNING_ROD));
    }

    @Override
    public void draw(LightningRodRecipeView recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blitSprite(SPRITE, 36, 15, 16, 16);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, LightningRodRecipeView recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 36 && mouseX < 36 + 16 && mouseY >= 15 && mouseY < 15 + 16) {
            tooltip.add(IntegrationComponents.LIGHTNING_STRIKE);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LightningRodRecipeView view, IFocusGroup focuses) {
        var recipe = view.recipe();
        view.input().ifPresent(in ->  builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(in));
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST).addItemStack(new ItemStack(Blocks.LIGHTNING_ROD));
        view.output().ifPresent(out ->  builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(out));
        var attachedSide = view.recipe().attachedSide();

        // Draw slot information
        int[] coordinates = DIRECTION_TO_LOCATION_MAP.get(attachedSide.orElse(Direction.UP));
        builder.addSlot(RecipeIngredientRole.INPUT, coordinates[0], coordinates[1])
                .addIngredients(WorkshopJeiPlugin.BLOCK_PREDICATE_TYPE, onePerInput(recipe));
        builder.addSlot(RecipeIngredientRole.CATALYST, coordinates[2], coordinates[3])
                .addIngredient(WorkshopJeiPlugin.BLOCK_PREDICATE_TYPE, catalyst(attachedSide));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 48 + 18, 14)
                .addIngredient(WorkshopJeiPlugin.BLOCK_PREDICATE_TYPE, fromOutput(recipe.output()));
    }

    private static List<BlockPredicate> onePerInput(LightningRodRecipe recipe) {
        var input = recipe.input();
        return recipe.blocks().stream().map(holder -> new BlockPredicate(
                Optional.of(HolderSet.direct(holder)),
                input.properties(),
                input.nbt()
        )).toList();
    }

    private static BlockPredicate catalyst(Optional<Direction> attachedFace) {
        var properties = StatePropertiesPredicate.Builder.properties()
                .hasProperty(LightningRodBlock.WATERLOGGED, false)
                .hasProperty(LightningRodBlock.POWERED, true);
        attachedFace.ifPresent(face -> properties.hasProperty(LightningRodBlock.FACING, face));

        return BlockPredicate.Builder.block()
                .of(Blocks.LIGHTNING_ROD)
                .setProperties(properties)
                .build();
    }

    private static BlockPredicate fromOutput(Block block) {
        return BlockPredicate.Builder.block().of(block).build();
    }
}
