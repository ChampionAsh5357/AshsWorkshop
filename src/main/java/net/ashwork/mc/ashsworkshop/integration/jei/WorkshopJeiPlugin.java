package net.ashwork.mc.ashsworkshop.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.ashwork.mc.ashsworkshop.integration.jei.lightningrod.LightningRodRecipeCategory;
import net.ashwork.mc.ashsworkshop.integration.jei.lightningrod.LightningRodRecipeView;
import net.ashwork.mc.ashsworkshop.recipe.LightningRodRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// TODO: Figure out how to handle without adding all blocks twice
@JeiPlugin
public class WorkshopJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = AshsWorkshop.fromMod("integration/jei");

    public static final IIngredientType<Block> BLOCK_TYPE = new IIngredientType<>() {
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
        var recipes = lightningRodRecipes().map(LightningRodRecipeView::new).toList();
        registration.addRecipes(LightningRodRecipeCategory.TYPE, recipes);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        var enabledFeatures = getSidedLevel().enabledFeatures();
        List<Block> allNonItemBlocks = lightningRodRecipes().flatMap(recipe -> {
            List<Block> blocks = new ArrayList<>();
            recipe.input().forEach(block -> {
                if (block.value().asItem() == Items.AIR) {
                    blocks.add(block.value());
                }
            });
            if (recipe.output().asItem() == Items.AIR) {
                blocks.add(recipe.output());
            }
            return blocks.stream();
        }).filter(block -> block.isEnabled(enabledFeatures)).toList();
        registration.register(BLOCK_TYPE, allNonItemBlocks, BLOCK_HELPER, new BlockIngredientRenderer());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LightningRodRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    private static Stream<LightningRodRecipe> lightningRodRecipes() {
        var level = getSidedLevel();

        return level == null ? Stream.empty()
                : level.getRecipeManager().getAllRecipesFor(RecipeRegistrar.LIGHTNING_ROD_TYPE.get())
                .stream().map(RecipeHolder::value).filter(recipe -> !recipe.isIncomplete());
    }

    @Nullable
    private static Level getSidedLevel() {
        if (FMLEnvironment.dist.isClient()) {
            return Minecraft.getInstance().level;
        }
        return ServerLifecycleHooks.getCurrentServer().overworld();
    }
}
