package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking.CenterMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking.CornerMarkingsRenderer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer.marking.MainMarkingRenderer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.SudokuBox;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;

public class SudokuObjectRendererTypes {

    public static void registerTypes(SudokuRendererHandler handler) {
        handler.registerRenderer(AshsWorkshop.fromMod("marking_main"), marking(MarkingRegistrar.MAIN.get()), MainMarkingRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_center"), marking(MarkingRegistrar.CENTER.get()), CenterMarkingsRenderer::new);
        handler.registerRenderer(AshsWorkshop.fromMod("marking_corner"), marking(MarkingRegistrar.CORNER.get()), CornerMarkingsRenderer::new);

        var boxTint = marking(MarkingRegistrar.BOX_TINT.get(), SudokuBoxLayer.BACKGROUND);
        var boxTintFog = marking(MarkingRegistrar.BOX_TINT.get(), SudokuBoxLayer.FOG_OF_WAR);
    }

    private static <M extends SudokuMarking, T extends SudokuMarking.Type<M>> SudokuObjectRenderer.Type<T, M> marking(T type) {
        return marking(type, SudokuBoxLayer.MARKINGS);
    }

    private static <M extends SudokuMarking, T extends SudokuMarking.Type<M>> SudokuObjectRenderer.Type<T, M> marking(T type, SudokuBoxLayer layer) {
        return new SudokuObjectRenderer.Type<>(type, SudokuBox::getMarking, layer);
    }
}
