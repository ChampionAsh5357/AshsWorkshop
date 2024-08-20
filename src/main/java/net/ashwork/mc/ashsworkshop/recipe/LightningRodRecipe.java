/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.ashwork.mc.ashsworkshop.recipe.block.AttachedBlockInput;
import net.ashwork.mc.ashsworkshop.recipe.block.BlockRecipe;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
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

import java.util.Optional;

public record LightningRodRecipe(String group, BlockPredicate input, Optional<Direction> attachedSide, Block output) implements BlockRecipe<AttachedBlockInput> {

    private static final Codec<BlockPredicate> NON_EMPTY_PREDICATE_CODEC = BlockPredicate.CODEC.validate(predicate ->
            predicate.blocks().isPresent() ? DataResult.success(predicate) : DataResult.error(() -> "Predicate has no blocks to check against", predicate)
    );
    public static final MapCodec<LightningRodRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(LightningRodRecipe::group),
                    NON_EMPTY_PREDICATE_CODEC.fieldOf("input").forGetter(LightningRodRecipe::input),
                    Direction.CODEC.optionalFieldOf("attached_side").forGetter(LightningRodRecipe::attachedSide),
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("output").forGetter(LightningRodRecipe::output)
            ).apply(instance, LightningRodRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, LightningRodRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, LightningRodRecipe::group,
            BlockPredicate.STREAM_CODEC, LightningRodRecipe::input,
            Direction.STREAM_CODEC.apply(ByteBufCodecs::optional), LightningRodRecipe::attachedSide,
            ByteBufCodecs.registry(Registries.BLOCK), LightningRodRecipe::output,
            LightningRodRecipe::new
    );

    @Override
    public boolean matches(AttachedBlockInput input, Level level) {
        return this.attachedSide.map(direction -> direction == input.attachedFace()).orElse(true) && this.input.matches(input.inWorld());
    }

    @Override
    public BlockState setState(AttachedBlockInput input, HolderLookup.Provider registries) {
        var result = this.output.defaultBlockState();
        var block = input.inWorld().getState();

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
        return this.blocks().size() == 0;
    }

    public HolderSet<Block> blocks() {
        return this.input.blocks().get();
    }
}
