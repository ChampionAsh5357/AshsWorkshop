/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalRegistrars;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.ashwork.mc.ashsworkshop.init.Registrars;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * The experimental common mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(AshsWorkshop.ID)
public class ExperimentalAshsWorkshop {

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public ExperimentalAshsWorkshop(IEventBus modBus) {
        ExperimentalWorkshopRegistries.registerRegistries(modBus);
        ExperimentalRegistrars.registerRegistrars(modBus);
    }
}
