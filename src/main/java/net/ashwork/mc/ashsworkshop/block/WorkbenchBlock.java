/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.block;

import net.ashwork.mc.ashsworkshop.menu.WorkbenchMenu;
import net.ashwork.mc.ashsworkshop.util.WorkshopComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * A block representing the workbench of this mod.
 */
public class WorkbenchBlock extends Block {

    /**
     * A simple constructor.
     *
     * @param properties the properties of the block
     */
    public WorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        // Create the workbench menu
        return new SimpleMenuProvider(
                (menuId, playerInventory, player) -> new WorkbenchMenu(menuId, playerInventory, ContainerLevelAccess.create(level, pos)),
                WorkshopComponents.WORKBENCH_MENU
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // Open menu on server side
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(state.getMenuProvider(level, pos));
        }
        // SUCCESS on client, CONSUME on server
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
