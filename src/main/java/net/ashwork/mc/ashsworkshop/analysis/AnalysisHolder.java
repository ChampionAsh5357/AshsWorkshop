package net.ashwork.mc.ashsworkshop.analysis;

import com.mojang.serialization.Codec;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.network.server.ClientboundUpdateAnalyzedResources;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A holder that contains information about the analyzed resources of the entity.
 */
public class AnalysisHolder {

    public static final Codec<AnalysisHolder> CODEC = Codec.unboundedMap(
            WorkshopRegistries.ANALYSIS.byNameCodec(),
            WorkshopCodecs.set(ResourceLocation.CODEC)
    ).xmap(AnalysisHolder::new, AnalysisHolder::analyzedResources);
    public static final StreamCodec<RegistryFriendlyByteBuf, Map<Analysis<?>, Set<ResourceLocation>>> STREAM_CODEC = ByteBufCodecs.map(
            HashMap::new, ByteBufCodecs.registry(WorkshopRegistries.ANALYSIS_KEY), ResourceLocation.STREAM_CODEC.apply(WorkshopCodecs.setStreamCodec())
    );

    private final Map<Analysis<?>, Set<ResourceLocation>> analyzedResources;
    private Analysis.Resource<?, ?> currentAnalysis;

    public AnalysisHolder() {
        this.analyzedResources = new HashMap<>();
        this.currentAnalysis = null;
    }

    private AnalysisHolder(Map<Analysis<?>, Set<ResourceLocation>> analyzedResources) {
        this();
        this.unlockResources(analyzedResources);
    }

    /**
     * {@return an unmodifiable map of the currently analyzed resources}
     */
    public Map<Analysis<?>, Set<ResourceLocation>> analyzedResources() {
        return Collections.unmodifiableMap(this.analyzedResources);
    }

    /**
     * Unlocks the provided resources, does not trigger any subsequent unlocking logic.
     *
     * @param analyzedResources the resources to unlock
     */
    public void unlockResources(Map<Analysis<?>, Set<ResourceLocation>> analyzedResources) {
        analyzedResources.forEach((analysis, resources) ->
                this.analyzedResources.computeIfAbsent(analysis, k -> new HashSet<>()).addAll(resources)
        );
    }

    /**
     * Sets the current list of analyzed resources. This should only be called the client.
     *
     * @param analyzedResources the list of resources to set
     */
    public void setResources(Map<Analysis<?>, Set<ResourceLocation>> analyzedResources) {
        this.clear();
        this.unlockResources(analyzedResources);
    }

    /**
     * Clears all analyzed resources.
     */
    public void clear() {
        this.analyzedResources.clear();
    }

    /**
     * Begins the analysis process of the current resource.
     *
     * @param resource the resources to analyze
     * @return {@code true} if the analysis could be started, {@code false} otherwise
     * @param <C> the type of the analysis context
     */
    public <C extends AnalysisContext> boolean analyze(Analysis.Resource<C, Analysis<C>> resource) {
        // Only analyze if we haven't already analyzed the resource
        // Do not analyze if there is a blocker
        if (this.isAnalyzed(resource) || !resource.canAnalyze()) {
            return false;
        }

        this.currentAnalysis = resource;
        return true;
    }

    /**
     * @param resource the resource to analyze
     * {@return {@code true} if the resource was already analyzed, {@code false} otherwise}
     * @param <C> the type of the analysis context
     */
    public <C extends AnalysisContext> boolean isAnalyzed(Analysis.Resource<C, Analysis<C>> resource) {
        return this.analyzedResources.getOrDefault(resource.analysis(), Collections.emptySet()).contains(resource.getResourceToAnalyze());
    }

    /**
     * Checks if an analysis has any analyzed resources.
     *
     * @param analysis the analysis performed
     * @return {@code true} if there are resources analyzed, {@code false} otherwise
     */
    public boolean hasAnalyzedResources(Analysis<?> analysis) {
        return !this.analyzedResources.getOrDefault(analysis, Collections.emptySet()).isEmpty();
    }

    /**
     * @param analysis the analysis being performed
     * @param resource the analyzed resource
     * {@return {@code true} if the resource is already analyzed, {@code false} otherwise}
     */
    public boolean isAnalyzed(Analysis<?> analysis, ResourceLocation resource) {
        return this.analyzedResources.getOrDefault(analysis, Collections.emptySet()).contains(resource);
    }

    /**
     * Finishes the analysis process by unlocking whatever was available on the resource.
     */
    public void finishAnalysis() {
        // Handle edge case
        if (this.currentAnalysis == null) {
            return;
        }

        if (this.currentAnalysis.verifyResourceConditions()) {
            var resource = this.currentAnalysis.getResourceToAnalyze();
            this.analyzedResources.computeIfAbsent(this.currentAnalysis.analysis(), a -> new HashSet<>())
                    .add(resource);

            // Send packet to client
            if (this.currentAnalysis.player() instanceof ServerPlayer sp) {
                PacketDistributor.sendToPlayer(sp, new ClientboundUpdateAnalyzedResources(this.currentAnalysis.analysis(), resource));
            }

            this.currentAnalysis.unlock();
        }

        this.stopAnalysis();
    }

    /**
     * Stops the current analysis.
     */
    public void stopAnalysis() {
        this.currentAnalysis = null;
    }

    public AnalysisHolder copy(IAttachmentHolder holder, HolderLookup.Provider provider) {
        var result = new AnalysisHolder();
        result.unlockResources(this.analyzedResources);
        return result;
    }
}
