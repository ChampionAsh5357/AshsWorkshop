/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client;

import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.network.server.ClientboundUpdateAnalyzedResources;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * A class that contains the handlers for client-bound payload.
 */
public class ClientPayloadHandler {

    /**
     * Handles the {@link ClientboundUpdateAnalyzedResources} payload.
     *
     * @param payload the payload instance
     * @param context the payload context
     */
    public static void onUpdateAnalyzedResources(ClientboundUpdateAnalyzedResources payload, IPayloadContext context) {
        // Read in previous properties and set screen to sudoku selection
        var holder = context.player().getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);
        if (payload.clear()) {
            holder.setResources(payload.analyzedResources());
        } else {
            holder.unlockResources(payload.analyzedResources());
        }
    }
}
