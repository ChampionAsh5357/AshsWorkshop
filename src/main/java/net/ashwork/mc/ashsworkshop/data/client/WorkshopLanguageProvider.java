/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.data.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.init.BlockRegistrar;
import net.ashwork.mc.ashsworkshop.util.WorkshopComponents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/**
 * An English (United States) localization provider for the 'ashs_workshop' mod.
 */
public class WorkshopLanguageProvider extends LanguageProvider {

    /**
     * A simple constructor.
     *
     * @param output the root output location of the pack
     */
    public WorkshopLanguageProvider(PackOutput output) {
        super(output, AshsWorkshop.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addBlock(BlockRegistrar.WORKBENCH, "Ash's Workbench");

        this.add(WorkshopComponents.WORKBENCH_MENU_KEY, "Ash's Workbench");
    }
}
