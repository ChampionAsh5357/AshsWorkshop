/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku.renderer;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.client.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.marking.BoxTintMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.marking.CenterMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.marking.CornerMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.marking.MainMarkingRenderer;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.selection.InvalidMarkingRenderer;
import net.ashwork.mc.ashsworkshop.client.sudoku.renderer.selection.SelectionRenderer;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;

/**
 * A class containing the renderers to apply to a given sudoku object.
 */
public class SudokuObjectRendererTypes {

    public static void registerTypes(SudokuRendererHandler handler) {
        handler.registerRenderer(AshsWorkshop.fromMod("marking_main"), marking(MarkingRegistrar.MAIN.get()), MainMarkingRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_center"), marking(MarkingRegistrar.CENTER.get()), CenterMarkingsRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_corner"), marking(MarkingRegistrar.CORNER.get()), CornerMarkingsRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_box_tint"), marking(MarkingRegistrar.BOX_TINT.get(), SudokuBoxLayer.BACKGROUND), () -> new BoxTintMarkingsRenderer(0xFFFFFFFF));
        handler.registerRenderer(AshsWorkshop.fromMod("invalid_marking"), marking(MarkingRegistrar.MAIN.get(), SudokuBoxLayer.SELECTION), InvalidMarkingRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("selection"), select(), SelectionRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_box_ting_fow"), marking(MarkingRegistrar.BOX_TINT.get(), SudokuBoxLayer.FOG_OF_WAR), () -> new BoxTintMarkingsRenderer(0xFF404040));
    }

    private static SudokuObjectRenderer.Type<Boolean, Boolean> select() {
        return new SudokuObjectRenderer.Type<>(false, (widget, b) -> widget.isSelected(), SudokuBoxLayer.SELECTION);
    }

    private static <M extends SudokuMarking, T extends SudokuMarking.Type<M>> SudokuObjectRenderer.Type<T, M> marking(T type) {
        return marking(type, SudokuBoxLayer.MARKINGS);
    }

    private static <M extends SudokuMarking, T extends SudokuMarking.Type<M>> SudokuObjectRenderer.Type<T, M> marking(T type, SudokuBoxLayer layer) {
        return new SudokuObjectRenderer.Type<>(type, (widget, t) -> widget.box().getMarking(t), layer);
    }
}
