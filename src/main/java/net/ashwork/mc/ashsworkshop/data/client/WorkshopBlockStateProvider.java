/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.data.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * A block state, block model, and item model generator for blocks for the 'ashs_workshop'
 * mod.
 */
public class WorkshopBlockStateProvider extends BlockStateProvider {

    /**
     * A simple constructor.
     *
     * @param output the root output location of the pack
     * @param exFileHelper the helper to read in any existing files
     */
    public WorkshopBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AshsWorkshop.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockWithItem(BlockRegistrar.WORKBENCH.value(),
                this.models().withExistingParent(
                        BlockRegistrar.WORKBENCH.getRegisteredName(),
                        this.blockTexture(Blocks.CRAFTING_TABLE)
                )
        );
    }
}
