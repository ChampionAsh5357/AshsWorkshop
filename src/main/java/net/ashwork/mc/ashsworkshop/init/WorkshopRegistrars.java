/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.analysis.AnalysisContext;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.game.sudoku.constraint.SudokuConstraint;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.commons.lang3.function.TriFunction;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A class for holding all the registrars used by this mod.
 */
public class WorkshopRegistrars {

    /**
     * The block registrar.
     */
    static final DeferredRegister.Blocks BLOCK = DeferredRegister.createBlocks(AshsWorkshop.ID);

    /**
     * The item registrar.
     */
    static final DeferredRegister.Items ITEM = DeferredRegister.createItems(AshsWorkshop.ID);

    /**
     * The menu type registrar.
     */
    static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(Registries.MENU, AshsWorkshop.ID);

    static final DeferredRegister<Analysis<?>> ANALYSIS = DeferredRegister.create(WorkshopRegistries.ANALYSIS, AshsWorkshop.ID);

    /**
     * The sudoku marking type registrar.
     */
    static final DeferredRegister<SudokuMarking.Type<?>> SUDOKU_MARKING_TYPE = DeferredRegister.create(WorkshopRegistries.SUDOKU_MARKING_TYPE, AshsWorkshop.ID);
    static final DeferredRegister<SudokuConstraint.Type<?>> SUDOKU_CONSTRAINT_TYPE = DeferredRegister.create(WorkshopRegistries.SUDOKU_CONSTRAINT_TYPE, AshsWorkshop.ID);
    static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPE = DeferredRegister.create(Registries.BLOCK_TYPE, AshsWorkshop.ID);
    static final DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, AshsWorkshop.ID);
    static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AshsWorkshop.ID);
    static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AshsWorkshop.ID);

    /**
     * Registers a block with an item.
     *
     * @param name the name of the block
     * @param blockFactory the factory to construct the block instance
     * @return a deferred, holder-wrapped block instance
     */
    static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> blockFactory) {
        var block = BLOCK.register(name, blockFactory);
        ITEM.registerSimpleBlockItem(block);
        return block;
    }

    /**
     * Registers the registrars to the mod event bus.
     *
     * @param modBus the mod event bus
     */
    public static void registerRegistrars(IEventBus modBus) {
        // Add registrars
        BLOCK.register(modBus);
        ITEM.register(modBus);
        MENU.register(modBus);
        SUDOKU_MARKING_TYPE.register(modBus);
        SUDOKU_CONSTRAINT_TYPE.register(modBus);
        BLOCK_TYPE.register(modBus);
        RECIPE_TYPE.register(modBus);
        RECIPE_SERIALIZER.register(modBus);
        ATTACHMENT_TYPE.register(modBus);
        ANALYSIS.register(modBus);

        // Load registry classes
        BlockRegistrar.register();
        ItemRegistrar.register();
        MenuRegistrar.register();
        MarkingRegistrar.register();
        ConstraintTypeRegistrar.register();
        RecipeRegistrar.register();
        AttachmentTypeRegistrar.register();
        AnalysisRegistrar.register();

        // Register events
        modBus.addListener(WorkshopRegistrars::buildTabs);
    }

    /**
     * Modifies existing creative tabs and adds items to them.
     *
     * @param event the event instance
     */
    private static void buildTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(BlockRegistrar.WORKBENCH.value());
        }
    }

    static <T> ResourceKey<T> dataKey(ResourceKey<? extends Registry<T>> registryKey, String name) {
        return ResourceKey.create(registryKey, AshsWorkshop.fromMod(name));
    }

    static <T> TagKey<T> tagKey(ResourceKey<? extends Registry<T>> registryKey, String name) {
        return TagKey.create(registryKey, AshsWorkshop.fromMod(name));
    }

    static <T> void tagProvider(ResourceKey<? extends Registry<T>> registryKey, Function<TriFunction<ExistingFileHelper, CompletableFuture<HolderLookup.Provider>, PackOutput, TagsProvider<T>>, TagsProvider<T>> factory, BiConsumer<HolderLookup.Provider, Function<TagKey<T>, TagsProvider.TagAppender<T>>> addTags) {
        factory.apply((fileHelper, registries, output) -> new TagsProvider<T>(output, registryKey, registries, AshsWorkshop.ID, fileHelper) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                addTags.accept(provider, this::tag);
            }
        });
    }
}
