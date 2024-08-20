/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.integration.util;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.network.chat.Component;

public interface IntegrationComponents {

    String INTEGRATION_JEI = "integration.jei";
    String LIGHTNING_STRIKE_KEY = jei("lightning_strike");

    Component LIGHTNING_STRIKE = Component.translatable(LIGHTNING_STRIKE_KEY);

    static String jei(String suffix) {
        return createWithIntegration(INTEGRATION_JEI, suffix);
    }

    static String createWithIntegration(String integration, String suffix) {
        return integration + "." + AshsWorkshop.ID + "." + suffix;
    }
}
