package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.alpha1.marking;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

public class ClientMarkingManager {

    private final Object2ObjectMap<MarkingClientHandler.RenderLayer, List<MarkingClientHandler<?>>> renderLayers;
    private final List<Pair<Integer, List<MarkingClientHandler<?>>>> interactionLayers;

    public ClientMarkingManager() {
        this.renderLayers = new Object2ObjectOpenHashMap<>();
        this.interactionLayers = new ArrayList<>();
    }

    public void registerHandler(MarkingClientHandler<?> handler) {
        this.renderLayers.computeIfAbsent(handler.layer(), k -> new ArrayList<>()).add(handler);

        boolean addedHandler = false;
        for (int i = 0; i < this.interactionLayers.size(); i++) {
            var layer = this.interactionLayers.get(i);
            if (layer.first() > handler.numOfModifiers()) {
                // If layer is greater than current index amount
                // Add new layer in-between
                List<MarkingClientHandler<?>> list = new ArrayList<>();
                list.add(handler);
                this.interactionLayers.add(i, Pair.of(handler.numOfModifiers(), list));
                addedHandler = true;
                break;
            } else if (layer.first() == handler.numOfModifiers()) {
                layer.second().add(handler);
                addedHandler = true;
                break;
            }
        }
        if (!addedHandler) {
            List<MarkingClientHandler<?>> list = new ArrayList<>();
            list.add(handler);
            this.interactionLayers.add(Pair.of(handler.numOfModifiers(), list));
        }
    }

    public List<Pair<Integer, List<MarkingClientHandler<?>>>> getInteractionLayers() {
        return this.interactionLayers;
    }

    public Object2ObjectMap<MarkingClientHandler.RenderLayer, List<MarkingClientHandler<?>>> getRenderLayers() {
        return this.renderLayers;
    }
}
