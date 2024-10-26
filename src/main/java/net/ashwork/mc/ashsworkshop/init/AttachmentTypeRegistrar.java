/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.init;

import com.mojang.serialization.Codec;
import net.ashwork.mc.ashsworkshop.analysis.AnalysisHolder;
import net.ashwork.mc.ashsworkshop.lock.SudokuLock;
import net.ashwork.mc.ashsworkshop.util.LambdaHelpers;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * The registrar for data attachments types.
 */
public class AttachmentTypeRegistrar {

    public static final Supplier<AttachmentType<AnalysisHolder>> ANALYSIS_HOLDER = register(
            "analysis_holder", AnalysisHolder::new, AnalysisHolder.CODEC, builder -> builder.copyOnDeath().copyHandler(AnalysisHolder::copy)
    );
    public static final Supplier<AttachmentType<SudokuLock>> SUDOKU_LOCK = register(
            "sudoku_lock", LambdaHelpers.throwOnInit(), SudokuLock.CODEC, AttachmentType.Builder::copyOnDeath
    );

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}

    private static <T> Supplier<AttachmentType<T>> register(String name, Supplier<T> factory, Codec<T> codec, UnaryOperator<AttachmentType.Builder<T>> builder) {
        return WorkshopRegistrars.ATTACHMENT_TYPE.register(name, () -> builder.apply(AttachmentType.builder(factory).serialize(codec)).build());
    }
}
