/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.util;

import net.ashwork.mc.ashsworkshop.init.MenuRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

// TODO: Document
public interface WorkshopComponents {

    String WORKBENCH_MENU_KEY = createFromRegistryObject(MenuRegistrar.WORKBENCH);

    Component WORKBENCH_MENU = Component.translatable(WORKBENCH_MENU_KEY);

    private static <T> String createFromRegistryObject(Holder<T> holder) {
        return holder.unwrapKey().map(key ->
                (key.registry().getNamespace().equals("minecraft") ? "" : key.registry().getNamespace() + ".")
                + key.registry().getPath() + "."
                + key.location().getNamespace() + "."
                + key.location().getPath() + "."
        ).orElseThrow();
    }
}
