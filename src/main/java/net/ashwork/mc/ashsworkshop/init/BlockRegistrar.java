/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.block.WorkbenchBlock;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * The registrar for blocks.
 */
public class BlockRegistrar {

    public static final DeferredBlock<Block> WORKBENCH = WorkshopRegistrars.registerBlockWithItem(
            "workbench", () -> new WorkbenchBlock(BlockBehaviour.Properties.of())
    );
    public static final Holder<MapCodec<? extends Block>> WORKBENCH_CODEC = WorkshopRegistrars.BLOCK_TYPE.register("workbench", () -> WorkbenchBlock.CODEC);

    /**
     * Returns all blocks registered by this mod.
     *
     * @return an iterable containing all blocks registered by this mod
     */
    public static Iterable<Block> all() {
        return WorkshopRegistrars.BLOCK.getEntries().stream()
                .map(DeferredHolder::value).map(Block.class::cast)::iterator;
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
