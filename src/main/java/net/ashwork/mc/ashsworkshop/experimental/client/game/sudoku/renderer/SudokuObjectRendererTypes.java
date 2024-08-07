package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking.BoxTintMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking.CenterMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking.CornerMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking.MainMarkingRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.selection.InvalidMarkingRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.selection.SelectionRenderer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;

public class SudokuObjectRendererTypes {

    public static void registerTypes(SudokuRendererHandler handler) {
        handler.registerRenderer(AshsWorkshop.fromMod("marking_main"), marking(MarkingRegistrar.MAIN.get()), MainMarkingRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_center"), marking(MarkingRegistrar.CENTER.get()), CenterMarkingsRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_corner"), marking(MarkingRegistrar.CORNER.get()), CornerMarkingsRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_box_tint"), marking(MarkingRegistrar.BOX_TINT.get(), SudokuBoxLayer.BACKGROUND), () -> new BoxTintMarkingsRenderer(0xFFFFFFFF));
        handler.registerRenderer(AshsWorkshop.fromMod("invalid_marking"), marking(MarkingRegistrar.MAIN.get(), SudokuBoxLayer.SELECTION), InvalidMarkingRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("selection"), select(), SelectionRenderer::new);

        var boxTintFog = marking(MarkingRegistrar.BOX_TINT.get(), SudokuBoxLayer.FOG_OF_WAR);
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
