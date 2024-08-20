/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.util;

import net.ashwork.mc.ashsworkshop.init.MenuRegistrar;
import net.ashwork.mc.ashsworkshop.init.RecipeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

// TODO: Document
public interface WorkshopComponents {

    String WORKBENCH_MENU_KEY = createFromRegistryObject(MenuRegistrar.WORKBENCH);
    String LIGHTNING_ROD_RECIPE_TYPE_KEY = createFromRegistryObject(RecipeRegistrar.LIGHTNING_ROD_TYPE);

    Component WORKBENCH_MENU = Component.translatable(WORKBENCH_MENU_KEY);
    Component LIGHTNING_ROD_RECIPE_TYPE = Component.translatable(LIGHTNING_ROD_RECIPE_TYPE_KEY);

    static <T> String createWithSuffix(ResourceKey<T> key, String suffix) {
        return createFromResourceKey(key) + "." + suffix;
    }

    static <T> String createFromRegistryKey(ResourceKey<? extends Registry<T>> key, String description) {
        return (key.location().getNamespace().equals("minecraft") ? "" : key.location().getNamespace() + ".")
                + key.location().getPath().replaceAll("/", "_") + "." + description;
    }

    private static <T> String createFromResourceKey(ResourceKey<T> key) {
        return (key.registry().getNamespace().equals("minecraft") ? "" : key.registry().getNamespace() + ".")
                + key.registry().getPath().replaceAll("/", "_") + "."
                + key.location().getNamespace() + "."
                + key.location().getPath().replaceAll("/", "_");
    }

    private static <T> String createFromRegistryObject(Holder<T> holder) {
        return holder.unwrapKey().map(WorkshopComponents::createFromResourceKey).orElseThrow();
    }
}
