package net.ashwork.mc.ashsworkshop.integration.jei.lightningrod;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.integration.jei.WorkshopJeiPlugin;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

// TODO: Finish implementing
public class LightningRodRecipeCategory implements IRecipeCategory<LightningRodRecipeView> {

    public static final RecipeType<LightningRodRecipeView> TYPE = RecipeType.create(
            AshsWorkshop.ID,
            "lightning_rod",
            LightningRodRecipeView.class
    );
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
        return Component.literal("Lightning Rod");
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(72, 42);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableItemStack(new ItemStack(Items.LIGHTNING_ROD));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LightningRodRecipeView view, IFocusGroup focuses) {
        var recipe = view.recipe();
        view.input().ifPresent(in ->  builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(in));
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST).addItemStack(new ItemStack(Blocks.LIGHTNING_ROD));
        view.output().ifPresent(out ->  builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(out));

        // Draw slot information
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 24)
                .addIngredients(WorkshopJeiPlugin.BLOCK_TYPE, recipe.input().stream().map(Holder::value).toList());
        builder.addSlot(RecipeIngredientRole.CATALYST, 6, 3)
                .addIngredient(WorkshopJeiPlugin.BLOCK_TYPE, Blocks.LIGHTNING_ROD);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 48, 14)
                .addIngredient(WorkshopJeiPlugin.BLOCK_TYPE, recipe.output());
    }
}
