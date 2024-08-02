/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * A class for holding all the registrars used by this mod.
 */
public class Registrars {

    /**
     * The block registrar.
     */
    static final DeferredRegister.Blocks BLOCK = DeferredRegister.createBlocks(AshsWorkshop.ID);

    /**
     * The item registrar.
     */
    static final DeferredRegister.Items ITEM = DeferredRegister.createItems(AshsWorkshop.ID);

    /**
     * The menu type registrar.
     */
    static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(Registries.MENU, AshsWorkshop.ID);

    /**
     * Registers a block with an item.
     *
     * @param name the name of the block
     * @param blockFactory the factory to construct the block instance
     * @return a deferred, holder-wrapped block instance
     */
    static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> blockFactory) {
        var block = BLOCK.register(name, blockFactory);
        ITEM.registerSimpleBlockItem(block);
        return block;
    }

    /**
     * Registers the registrars to the mod event bus.
     *
     * @param modBus the mod event bus
     */
    public static void registerRegistrars(IEventBus modBus) {
        // Add registrars
        BLOCK.register(modBus);
        ITEM.register(modBus);
        MENU.register(modBus);

        // Load registry classes
        BlockRegistrar.register();
        MenuRegistrar.register();

        // Register events
        modBus.addListener(Registrars::buildTabs);
    }

    /**
     * Modifies existing creative tabs and adds items to them.
     *
     * @param event the event instance
     */
    private static void buildTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(BlockRegistrar.WORKBENCH.value());
        }
    }
}
