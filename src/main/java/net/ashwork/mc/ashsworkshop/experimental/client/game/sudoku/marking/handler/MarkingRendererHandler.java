package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.marking.handler;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.RenderingCache;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.marking.MarkingRenderer;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.SudokuBox;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MarkingRendererHandler {

    private static final RenderingCache DUMMY = new RenderingCache();

    private final SortedMap<SudokuBoxLayer, Object2ObjectMap<SudokuMarking.Type<?>, MarkingRenderer<?>>> renderers;

    public MarkingRendererHandler() {
        this.renderers = new TreeMap<>(Comparator.naturalOrder());
    }

    public <T extends SudokuMarking> void registerRenderer(Supplier<MarkingRenderer<T>> factory) {
        var renderer = factory.get();
        if (this.renderers.computeIfAbsent(renderer.layer(), l -> new Object2ObjectLinkedOpenHashMap<>()).putIfAbsent(renderer.type(), renderer) != null) {
            throw new IllegalArgumentException("Marking renderer already registered for type: " + ExperimentalWorkshopRegistries.SUDOKU_MARKING_TYPE.getKey(renderer.type()));
        }
    }

    public void renderLayers(GuiGraphics graphics, SudokuBox box, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        for (var layer : this.renderers.entrySet()) {
            boolean stopRendering = false;
            for (var renderer : layer.getValue().values()) {
                stopRendering |= this.renderMarking(renderer, graphics, box, font, invalidChecker, x, y, width, height, selectedBorder, margin, locked);
            }

            if (stopRendering) {
                break;
            }
        }
    }

    private <T extends SudokuMarking> boolean renderMarking(MarkingRenderer<T> renderer, GuiGraphics graphics, SudokuBox box, Font font, Predicate<Character> invalidChecker, int x, int y, int width, int height, int selectedBorder, float margin, boolean locked) {
        T marking = box.getMarking(renderer.type());
        return renderer.render(graphics, marking, DUMMY, font, invalidChecker, x, y, width, height, selectedBorder, margin, locked);
    }
}
