/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.data.server;

import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Collections;

/**
 * A sub provider for block loot tables for the 'ashs_workshop' mod.
 */
public class WorkshopBlockLootSubProvider extends BlockLootSubProvider {

    /**
     * A simple constructor.
     *
     * @param registries the view of the registries
     */
    public WorkshopBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistrar.WORKBENCH.value());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistrar.all();
    }
}
