/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop;

import net.ashwork.mc.ashsworkshop.command.WorkshopCommand;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.SudokuNetworkPayloads;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistrars;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.network.NetworkPayloads;
import net.ashwork.mc.ashsworkshop.network.server.ClientboundUpdateAnalyzedResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
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

        var forgeBus = NeoForge.EVENT_BUS;
        forgeBus.addListener(AshsWorkshop::onPlayerLoggedIn);
        forgeBus.addListener(AshsWorkshop::registerCommands);
    }

    public static ResourceLocation fromMod(String path) {
        return ResourceLocation.fromNamespaceAndPath(AshsWorkshop.ID, path);
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        WorkshopCommand.register(event.getDispatcher(), event.getBuildContext());
    }

    private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        NetworkPayloads.register(registrar);
        SudokuNetworkPayloads.register(registrar);
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            PacketDistributor.sendToPlayer(sp, new ClientboundUpdateAnalyzedResources(
                    sp.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER).analyzedResources(), true
            ));
        }
    }
}
