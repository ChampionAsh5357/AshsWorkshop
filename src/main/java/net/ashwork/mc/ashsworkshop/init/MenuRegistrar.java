/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.menu.WorkbenchMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * The registrar for menu types.
 */
public class MenuRegistrar {

    public static final DeferredHolder<MenuType<?>, MenuType<WorkbenchMenu>> WORKBENCH =
            WorkshopRegistrars.MENU.register("workbench", () -> new MenuType<>(WorkbenchMenu::new, FeatureFlags.VANILLA_SET));

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
