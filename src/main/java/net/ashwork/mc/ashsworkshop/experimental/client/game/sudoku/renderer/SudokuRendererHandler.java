package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.renderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.SudokuBoxLayer;
import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.screen.widget.SudokuBoxWidget;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.SudokuBox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.toposort.TopologicalSort;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class SudokuRendererHandler {

    private final BiMap<ResourceLocation, SudokuObjectRenderer.Type<?, ?>> types;
    private final Map<SudokuObjectRenderer.Type<?, ?>, SudokuObjectRenderer<?>> renderers;
    private final SortedMap<SudokuBoxLayer, MutableGraph<SudokuObjectRenderer.Type<?, ?>>> renderingOrder;
    private List<SudokuObjectRenderer.Type<?, ?>> orderedRenderers;

    public SudokuRendererHandler() {
        this.types = HashBiMap.create();
        this.renderers = new Object2ObjectOpenHashMap<>();
        this.renderingOrder = new TreeMap<>(Comparator.naturalOrder());
    }

    public <T, V> void registerRenderer(ResourceLocation name, SudokuObjectRenderer.Type<T, V> type, Supplier<SudokuObjectRenderer<V>> factory) {
        registerRenderer(name, type, factory, Collections.emptyList(), Collections.emptyList());
    }

    public <T, V> void registerRendererAfter(ResourceLocation name, SudokuObjectRenderer.Type<T, V> type, Supplier<SudokuObjectRenderer<V>> factory, List<Object> befores) {
        registerRenderer(name, type, factory, Collections.emptyList(), befores);
    }

    public <T, V> void registerRendererBefore(ResourceLocation name, SudokuObjectRenderer.Type<T, V> type, Supplier<SudokuObjectRenderer<V>> factory, List<Object> afters) {
        registerRenderer(name, type, factory, afters, Collections.emptyList());
    }

    public <T, V> void registerRenderer(ResourceLocation name, SudokuObjectRenderer.Type<T, V> type, Supplier<SudokuObjectRenderer<V>> factory, List<Object> afters, List<Object> befores) {
        // First add types to registry
        if (this.types.putIfAbsent(name, type) != null) {
            throw new IllegalStateException("Duplicate sudoku object type: " + name);
        }
        this.renderers.put(type, factory.get());

        // Then add graph info
        MutableGraph<SudokuObjectRenderer.Type<?, ?>> graph = this.renderingOrder.computeIfAbsent(type.layer(), l -> GraphBuilder.directed().nodeOrder(ElementOrder.insertion()).build());
        graph.addNode(type);
        afters.forEach(after -> {
            var connection = this.getAndVerifyType(after, type.layer());
            graph.addNode(connection);
            graph.putEdge(connection, type);
        });
        befores.forEach(before -> {
            var connection = this.getAndVerifyType(before, type.layer());
            graph.addNode(connection);
            graph.putEdge(type, connection);
        });
    }

    private SudokuObjectRenderer.Type<?, ?> getAndVerifyType(Object obj, SudokuBoxLayer layer) {
        SudokuObjectRenderer.Type<?, ?> type = switch (obj) {
            case String s -> this.types.get(ResourceLocation.parse(s));
            case ResourceLocation rl -> this.types.get(rl);
            case SudokuObjectRenderer.Type<?, ?> t -> t;
            default -> throw new IllegalArgumentException("Unknown object type: " + obj.getClass());
        };
        Objects.requireNonNull(type);

        if (type.layer() != layer) {
            throw new IllegalArgumentException(this.types.inverse().get(type) + " is not in the same layer as current renderer: " + type.layer() + " instead of " + layer);
        }

        return type;
    }

    public void finalizeOrder() {
        ImmutableList.Builder<SudokuObjectRenderer.Type<?, ?>> builder = ImmutableList.builder();
        this.renderingOrder.values().forEach(graph -> builder.addAll(TopologicalSort.topologicalSort(graph, null)));
        this.orderedRenderers = builder.build();
    }

    public void render(GuiGraphics graphics, SudokuBoxWidget widget) {
        for (var type : this.orderedRenderers) {
            if (this.renderObject(type, graphics, widget)) {
                break;
            }
        }
    }

    private <T, O> boolean renderObject(SudokuObjectRenderer.Type<T, O> type, GuiGraphics graphics, SudokuBoxWidget widget) {
        var renderer = this.getRenderer(type);
        return renderer.render(graphics, type.get(widget), widget.font(), widget::containsInvalidValue, widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), widget.getSelectedBorder(), widget.getMargin(), widget.locked());
    }

    @SuppressWarnings("unchecked")
    private <T, O> SudokuObjectRenderer<O> getRenderer(SudokuObjectRenderer.Type<T, O> type) {
        return (SudokuObjectRenderer<O>) this.renderers.get(type);
    }
}
