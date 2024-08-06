/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.marking.handler.MarkingInteractionHandler;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.SudokuObjectRendererTypes;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.SudokuRendererHandler;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.lwjgl.glfw.GLFW;

/**
 * The experimental client mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(value = AshsWorkshop.ID, dist = Dist.CLIENT)
public class ExperimentalAshsWorkshopClient {

    private static ExperimentalAshsWorkshopClient _instance;
    private final MarkingInteractionHandler miHandler;
    private final SudokuRendererHandler srHandler;

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public ExperimentalAshsWorkshopClient(IEventBus modBus) {
        _instance = this;
        this.miHandler = new MarkingInteractionHandler();
        this.srHandler = new SudokuRendererHandler();

        modBus.addListener(this::clientSetup);
        modBus.addListener(this::loadComplete);
    }

    public static ExperimentalAshsWorkshopClient instance() {
        return _instance;
    }

    public MarkingInteractionHandler markingInteractionHandler() {
        return this.miHandler;
    }

    public SudokuRendererHandler sudokuRendererHandler() {
        return this.srHandler;
    }

    /**
     * Handles logic that should be executed during the client setup phase.
     *
     * @param event the event instance
     */
    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            SudokuObjectRendererTypes.registerTypes(this.srHandler);

            this.miHandler.registerModifiers(MarkingRegistrar.MAIN.get());
            this.miHandler.registerModifiers(MarkingRegistrar.CORNER.get(), GLFW.GLFW_MOD_SHIFT);
            this.miHandler.registerModifiers(MarkingRegistrar.CENTER.get(), GLFW.GLFW_MOD_CONTROL);
        });
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(this.srHandler::finalizeOrder);
    }
}
