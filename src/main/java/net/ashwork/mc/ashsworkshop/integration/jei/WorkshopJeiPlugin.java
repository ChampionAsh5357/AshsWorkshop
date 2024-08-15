package net.ashwork.mc.ashsworkshop.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

// TODO: Figure out how to handle without adding all blocks twice
@JeiPlugin
public class WorkshopJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = AshsWorkshop.fromMod("integration/jei");

    static final IIngredientType<Block> BLOCK_TYPE = new IIngredientType<>() {
        @Override
        public Class<? extends Block> getIngredientClass() {
            return Block.class;
        }

        @Override
        public String getUid() {
            return RecipeRegistrar.LIGHTNING_ROD_TYPE.get().toString();
        }
    };
    static final IIngredientHelper<Block> BLOCK_HELPER = new BlockStackHelper();

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = getSidedLevel();

        registration.addRecipes(LightningRodRecipeCategory.TYPE,
                level == null ? Collections.emptyList()
                        : level.getRecipeManager().getAllRecipesFor(RecipeRegistrar.LIGHTNING_ROD_TYPE.get())
                        .stream().filter(recipe -> !recipe.value().isIncomplete()).toList());
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(BLOCK_TYPE, BuiltInRegistries.BLOCK.asLookup()
                .filterFeatures(getSidedLevel().enabledFeatures()).listElements().map(Holder::value).toList(), BLOCK_HELPER, new BlockIngredientRenderer());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LightningRodRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRuntime(IRuntimeRegistration registration) {
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(new ItemStack(BlockRegistrar.WORKBENCH.get())));
    }

    @Nullable
    private static Level getSidedLevel() {
        if (FMLEnvironment.dist.isClient()) {
            return Minecraft.getInstance().level;
        }
        return ServerLifecycleHooks.getCurrentServer().overworld();
    }
}
