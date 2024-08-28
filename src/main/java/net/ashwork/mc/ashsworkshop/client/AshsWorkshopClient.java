/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.client.item.AnalyzerClientItem;
import net.ashwork.mc.ashsworkshop.client.screen.WorkbenchScreen;
import net.ashwork.mc.ashsworkshop.client.sudoku.marking.handler.MarkingInteractionHandler;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.SudokuObjectRendererTypes;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.SudokuRendererHandler;
import net.ashwork.mc.ashsworkshop.init.ItemRegistrar;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.init.MenuRegistrar;
import net.ashwork.mc.ashsworkshop.menu.WorkbenchMenu;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.lwjgl.glfw.GLFW;

/**
 * The client mod entrypoint for the 'ashs_workshop' mod.
 */
@Mod(value = AshsWorkshop.ID, dist = Dist.CLIENT)
public class AshsWorkshopClient {

    private static AshsWorkshopClient _instance;
    private final MarkingInteractionHandler miHandler;
    private final SudokuRendererHandler srHandler;

    /**
     * The constructor entrypoint for the 'ashs_workshop' mod.
     *
     * @param modBus the mod event bus
     */
    public AshsWorkshopClient(IEventBus modBus) {
        _instance = this;
        this.miHandler = new MarkingInteractionHandler();
        this.srHandler = new SudokuRendererHandler();

        modBus.addListener(this::clientSetup);
        modBus.addListener(this::loadComplete);
        modBus.addListener(this::registerMenuScreens);
        modBus.addListener(this::registerClientExtensions);
    }

    /**
     * {@return a static reference to this instance}
     */
    public static AshsWorkshopClient instance() {
        return _instance;
    }

    /**
     * {@return the interaction handler for sudoku markings}
     */
    public MarkingInteractionHandler markingInteractionHandler() {
        return this.miHandler;
    }

    /**
     * {@return the object renderer handler for sudoku grids}
     */
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

            ItemProperties.register(
                    ItemRegistrar.ANALYZER.get(), ItemRegistrar.ANALYZING_PROPERTY,
                    (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1 : 0
            );
            ItemProperties.register(
                    ItemRegistrar.ANALYZER.get(), ItemRegistrar.STATUS_PROPERTY,
                    (stack, level, entity, seed) -> entity != null && entity.getUseItem() == stack ? (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / stack.getUseDuration(entity) : 0
            );
        });
    }

    /**
     * Registers the client extensions for a given item.
     *
     * @param event the event instance
     */
    private void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new AnalyzerClientItem(), ItemRegistrar.ANALYZER.get());
    }

    /**
     * Handles logic that should be executed just before the game finishes loading.
     *
     * @param event the event instance
     */
    private void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(this.srHandler::finalizeOrder);
    }

    /**
     * Registers screens associated with a menu.
     *
     * @param event the event instance
     */
    private void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(MenuRegistrar.WORKBENCH.value(), (WorkbenchMenu menu, Inventory inventory, Component title) -> new WorkbenchScreen(menu, title));
    }

    private void renderLevelStage(RenderLevelStageEvent event) {
        // TODO: Figure out how to add analyzer target information
    }
}
