/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.client.screen.WorkbenchScreen;
import net.ashwork.mc.ashsworkshop.init.MenuRegistrar;
import net.ashwork.mc.ashsworkshop.menu.WorkbenchMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * The client mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(value = AshsWorkshop.ID, dist = Dist.CLIENT)
public class AshsWorkshopClient {

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public AshsWorkshopClient(IEventBus modBus) {
        modBus.addListener(this::registerMenuScreens);
    }

    /**
     * Registers screens associated with a menu.
     *
     * @param event the event instance
     */
    private void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(MenuRegistrar.WORKBENCH.value(), (WorkbenchMenu menu, Inventory inventory, Component title) -> new WorkbenchScreen(menu, title));
    }
}
