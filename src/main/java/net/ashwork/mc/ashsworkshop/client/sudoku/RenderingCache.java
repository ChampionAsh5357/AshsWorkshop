package net.ashwork.mc.ashsworkshop.client.sudoku;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.function.Supplier;

// Should be flushed during changes
public class RenderingCache {

    private final Object2ObjectMap<Key<?>, Object> objects;

    public RenderingCache() {
        this.objects = new Object2ObjectOpenHashMap<>();
    }

    public <T> T getOrCache(Key<T> key, Supplier<T> toCache) {
        @SuppressWarnings("unchecked")
        T val = (T) this.objects.computeIfAbsent(key, k -> toCache.get());
        return val;
    }

    public void invalidateKey(Key<?> key) {
        this.objects.remove(key);
    }

    public void invalidateCache() {
        this.objects.clear();
    }

    public record Key<T>() {}
}
