package net.ashwork.mc.ashsworkshop.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.ashwork.mc.ashsworkshop.recipe.LightningRodRecipe;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

// TODO: Finish implementing
public class LightningRodRecipeCategory implements IRecipeCategory<RecipeHolder<LightningRodRecipe>> {

    static final RecipeType<RecipeHolder<LightningRodRecipe>> TYPE = RecipeType.createFromVanilla(RecipeRegistrar.LIGHTNING_ROD_TYPE.get());
    private final IGuiHelper guiHelper;

    public LightningRodRecipeCategory(IGuiHelper helper) {
        this.guiHelper = helper;
    }

    @Override
    public RecipeType<RecipeHolder<LightningRodRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Lightning Rod");
    }

    @Override
    public IDrawable getBackground() {
        // TODO: figure out if this needs to change
        return this.guiHelper.createBlankDrawable(100, 100);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableItemStack(new ItemStack(Items.LIGHTNING_ROD));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<LightningRodRecipe> holder, IFocusGroup focuses) {
        var recipe = holder.value();
        // TODO: Adjust values as necessary
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 20)
                .addIngredients(WorkshopJeiPlugin.BLOCK_TYPE, recipe.input().stream().map(Holder::value).toList());
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 0)
                .addIngredient(WorkshopJeiPlugin.BLOCK_TYPE, Blocks.LIGHTNING_ROD);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 20, 0)
                .addIngredient(WorkshopJeiPlugin.BLOCK_TYPE, recipe.output());
    }
}
