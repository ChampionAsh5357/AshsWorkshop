/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop;

import net.ashwork.mc.ashsworkshop.init.Registrars;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

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
        Registrars.registerRegistrars(modBus);
    }

    // TODO: Document
    public static ResourceLocation fromMod(String path) {
        return ResourceLocation.fromNamespaceAndPath(AshsWorkshop.ID, path);
    }
}
