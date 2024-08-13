package net.ashwork.mc.ashsworkshop.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.ashwork.mc.ashsworkshop.recipe.block.BlockInput;
import net.ashwork.mc.ashsworkshop.recipe.block.BlockRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public record LightningRodRecipe(String group, HolderSet<Block> input, Block output) implements BlockRecipe<BlockInput> {

    public static final MapCodec<LightningRodRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(LightningRodRecipe::group),
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("input").forGetter(LightningRodRecipe::input),
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("output").forGetter(LightningRodRecipe::output)
            ).apply(instance, LightningRodRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, LightningRodRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, LightningRodRecipe::group,
            ByteBufCodecs.holderSet(Registries.BLOCK), LightningRodRecipe::input,
            ByteBufCodecs.registry(Registries.BLOCK), LightningRodRecipe::output,
            LightningRodRecipe::new
    );

    @Override
    public boolean matches(BlockInput input, Level level) {
        return input.getBlock(0).is(this.input);
    }

    @Override
    public BlockState setState(BlockInput input, HolderLookup.Provider registries) {
        var result = this.output.defaultBlockState();
        var block = input.getBlock(0);

        // Copy any similar properties
        for (var property : block.getProperties()) {
            if (result.hasProperty(property)) {
                result = setProperty(result, block, property);
            }
        }

        // Return final state
        return result;
    }

    private static <T extends Comparable<T>> BlockState setProperty(BlockState result, BlockState input, Property<T> property) {
        return result.setValue(property, input.getValue(property));
    }

    @Override
    public BlockState getResultState(HolderLookup.Provider registries) {
        return this.output.defaultBlockState();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.LIGHTNING_ROD);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistrar.LIGHTNING_ROD_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistrar.LIGHTNING_ROD_TYPE.get();
    }

    @Override
    public boolean isIncomplete() {
        return this.input.size() == 0;
    }
}
