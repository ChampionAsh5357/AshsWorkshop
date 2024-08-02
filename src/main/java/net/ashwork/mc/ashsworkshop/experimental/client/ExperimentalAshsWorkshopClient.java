/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking.CenterMarkingsClientHandler;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking.ClientMarkingManager;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking.CornerMarkingsClientHandler;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking.MainMarkingClientHandler;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.marking.CenterMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.marking.CornerMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.marking.MainMarkingRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.marking.handler.MarkingInteractionHandler;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha2.marking.handler.MarkingRendererHandler;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;

/**
 * The experimental client mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(value = AshsWorkshop.ID, dist = Dist.CLIENT)
public class ExperimentalAshsWorkshopClient {

    private static ExperimentalAshsWorkshopClient _instance;
    private final ClientMarkingManager markingManager;
    private final MarkingInteractionHandler miHandler;
    private final MarkingRendererHandler mrHandler;

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public ExperimentalAshsWorkshopClient(IEventBus modBus) {
        _instance = this;
        this.markingManager = new ClientMarkingManager();
        this.miHandler = new MarkingInteractionHandler();
        this.mrHandler = new MarkingRendererHandler();

        modBus.addListener(this::clientSetup);
    }

    public static ExperimentalAshsWorkshopClient instance() {
        return _instance;
    }

    /**
     * {@return the marking manager}
     */
    public ClientMarkingManager getMarkingManager() {
        return markingManager;
    }

    public MarkingInteractionHandler markingInteractionHandler() {
        return this.miHandler;
    }

    public MarkingRendererHandler markingRendererHandler() {
        return this.mrHandler;
    }

    /**
     * Handles logic that should be executed during the client setup phase.
     *
     * @param event the event instance
     */
    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
           this.markingManager.registerHandler(new MainMarkingClientHandler());
           this.markingManager.registerHandler(new CenterMarkingsClientHandler());
           this.markingManager.registerHandler(new CornerMarkingsClientHandler());

           this.mrHandler.registerRenderer(MainMarkingRenderer::new);
           this.mrHandler.registerRenderer(CenterMarkingsRenderer::new);
           this.mrHandler.registerRenderer(CornerMarkingsRenderer::new);

           this.miHandler.registerModifiers(MarkingRegistrar.MAIN.get());
           this.miHandler.registerModifiers(MarkingRegistrar.CORNER.get(), GLFW.GLFW_MOD_SHIFT);
           this.miHandler.registerModifiers(MarkingRegistrar.CENTER.get(), GLFW.GLFW_MOD_CONTROL);
        });
    }
}
