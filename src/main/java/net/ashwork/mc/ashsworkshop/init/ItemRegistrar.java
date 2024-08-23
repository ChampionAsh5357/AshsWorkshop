/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.item.AnalyzerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * The registrar for items.
 */
public class ItemRegistrar {

    public static final ResourceLocation ANALYZING_PROPERTY = AshsWorkshop.fromMod("analyzing");
    public static final ResourceLocation STATUS_PROPERTY = AshsWorkshop.fromMod("status");

    public static final DeferredItem<AnalyzerItem> ANALYZER = WorkshopRegistrars.ITEM.registerItem(
            "analyzer", AnalyzerItem::new, new Item.Properties()
    );

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
