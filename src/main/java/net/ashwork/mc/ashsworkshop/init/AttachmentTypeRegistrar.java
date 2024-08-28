/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import net.ashwork.mc.ashsworkshop.analysis.AnalysisHolder;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

/**
 * The registrar for data attachments types.
 */
public class AttachmentTypeRegistrar {

    public static final Supplier<AttachmentType<AnalysisHolder>> ANALYSIS_HOLDER = WorkshopRegistrars.ATTACHMENT_TYPE.register("analysis_holder",
            () -> AttachmentType.serializable(AnalysisHolder::new).copyOnDeath().copyHandler(AnalysisHolder::copy).build());

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
