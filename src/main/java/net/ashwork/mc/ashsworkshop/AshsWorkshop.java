/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop;

import net.ashwork.mc.ashsworkshop.game.sudoku.network.SudokuNetworkPayloads;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistrars;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * The common mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(AshsWorkshop.ID)
public class AshsWorkshop {

    /**
     * The mod identifiyer for the 'ashs_workshop' mod.
     */
    public static final String ID = "ashs_workshop";

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public AshsWorkshop(IEventBus modBus) {
        WorkshopRegistries.registerRegistries(modBus);
        WorkshopRegistrars.registerRegistrars(modBus);

        modBus.addListener(AshsWorkshop::registerPayloadHandlers);
    }

    // TODO: Document
    public static ResourceLocation fromMod(String path) {
        return ResourceLocation.fromNamespaceAndPath(AshsWorkshop.ID, path);
    }

    private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        SudokuNetworkPayloads.register(registrar);
    }
}
