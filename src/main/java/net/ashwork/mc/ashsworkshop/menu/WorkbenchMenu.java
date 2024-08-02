/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.menu;

import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.ashwork.mc.ashsworkshop.init.MenuRegistrar;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

// TODO: Document
public class WorkbenchMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    public WorkbenchMenu(int menuId, Inventory playerInventory) {
        this(menuId, playerInventory, ContainerLevelAccess.NULL);
    }

    public WorkbenchMenu(int menuId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuRegistrar.WORKBENCH.value(), menuId);
        this.access = access;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, BlockRegistrar.WORKBENCH.value());
    }
}
