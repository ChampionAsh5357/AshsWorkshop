package net.ashwork.mc.ashsworkshop.analysis;

import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.network.server.ClientboundUpdateAnalyzedResources;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A holder, only applicable to a player, that contains the information about the analyzing reosurces.
 */
public class AnalysisHolder implements INBTSerializable<ListTag> {

    private final Player player;
    private final Set<ResourceLocation> analyzedResources;

    private Analysis<?> analyzing;
    private AnalysisContext context;

    public AnalysisHolder(IAttachmentHolder holder) {
        this.player = (Player) holder;
        this.analyzedResources = new HashSet<>();
    }

    // Start analyzing the thing
    public <C extends AnalysisContext, A extends Analysis<C>> boolean analyze(A analyzing, C context) {
        // Do not analyze if it already has been
        if (analyzing.storeInHolder() && this.isAnalyzed(analyzing)) {
            return false;
        }

        // Make sure there isn't anything analysis specific blocking it
        if (!analyzing.canAnalyze(this.player, context)) {
            return false;
        }

        this.analyzing = analyzing;
        this.context = context;
        return true;
    }

    // When the analysis is finished
    public void finishAnalyzing() {
        // Handle edge case
        if (this.analyzing == null || this.context == null) {
            this.stopAnalyzing();
            return;
        }

        if (this.context.validate()) {
            this.unlock();
        }

        this.stopAnalyzing();
    }

    public boolean isAnalyzed(Analysis<?> analysis) {
        return this.analyzedResources.contains(analysisKey(analysis));
    }

    public Set<ResourceLocation> analyzedResources() {
        return Collections.unmodifiableSet(this.analyzedResources);
    }

    public void setResources(Set<ResourceLocation> analyzedResources) {
        this.clear();
        this.analyzedResources.addAll(analyzedResources);
    }

    public boolean unlockResources(Set<ResourceLocation> analyzedResources) {
        return this.analyzedResources.addAll(analyzedResources);
    }

    public boolean remove(ResourceLocation resource) {
        return this.analyzedResources.remove(resource);
    }

    public void clear() {
        this.analyzedResources.clear();
    }

    // Remove reference to objects
    public void stopAnalyzing() {
        this.analyzing = null;
        this.context = null;
    }

    private <C extends AnalysisContext, A extends Analysis<C>> void unlock() {
        @SuppressWarnings("unchecked")
        C context = (C) this.context;
        @SuppressWarnings("unchecked")
        A analysis = (A) this.analyzing;

        if (analysis.storeInHolder()) {
            var key = analysisKey(analysis);
            this.analyzedResources.add(key);

            // Send single key
            if (this.player instanceof ServerPlayer sp) {
                PacketDistributor.sendToPlayer(sp, new ClientboundUpdateAnalyzedResources(Set.of(key)));
            }
        }
        analysis.unlock(this.player, context);
    }

    private static ResourceLocation analysisKey(Analysis<?> analysis) {
        return WorkshopRegistries.ANALYSIS.getKey(analysis);
    }

    @UnknownNullability
    @Override
    public ListTag serializeNBT(HolderLookup.Provider provider) {
        ListTag tag = new ListTag();
        this.analyzedResources.forEach(name -> tag.add(StringTag.valueOf(name.toString())));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, ListTag nbt) {
        nbt.forEach(tag -> this.analyzedResources.add(ResourceLocation.parse(tag.getAsString())));
    }

    public AnalysisHolder copy(IAttachmentHolder holder, HolderLookup.Provider provider) {
        var result = new AnalysisHolder(holder);
        result.unlockResources(this.analyzedResources);
        return result;
    }
}
