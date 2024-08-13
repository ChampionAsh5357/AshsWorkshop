package net.ashwork.mc.ashsworkshop.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.recipe.LightningRodRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

/**
 * The registrar for recipes.
 */
public class RecipeRegistrar {

    public static final Supplier<RecipeType<LightningRodRecipe>> LIGHTNING_ROD_TYPE = type("lightning_rod");
    public static final Supplier<RecipeSerializer<LightningRodRecipe>> LIGHTNING_ROD_SERIALIZER = serializer("lightning_rod", LightningRodRecipe.CODEC, LightningRodRecipe.STREAM_CODEC);

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}

    private static <T extends Recipe<?>> Supplier<RecipeType<T>> type(String name) {
        return WorkshopRegistrars.RECIPE_TYPE.register(name, () -> RecipeType.simple(AshsWorkshop.fromMod(name)));
    }

    private static <T extends Recipe<?>> Supplier<RecipeSerializer<T>> serializer(String name, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return WorkshopRegistrars.RECIPE_SERIALIZER.register(name, () -> new RecipeSerializer<>() {
            @Override
            public MapCodec<T> codec() {
                return codec;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        });
    }
}
