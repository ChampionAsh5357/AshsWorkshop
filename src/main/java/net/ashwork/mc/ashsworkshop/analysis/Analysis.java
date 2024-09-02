package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.stream.Stream;

/**
 * Indicates a thing that can be analyzed for unlockable information.
 *
 * @param <C> the context of what is being analyzed
 */
public interface Analysis<C extends AnalysisContext> {

    /**
     * @param context the context being analyzed
     * {@return {@code true} if the context can be analyzed, {@code false} otherwise}
     */
    default boolean canAnalyze(C context) {
        return true;
    }

    /**
     * Unlocks the analysis resource for any additional effects that should be applied.
     *
     * @param context the context being analyzed
     */
    void unlock(C context);

    /**
     * @param context the context being analyzed
     * {@return the resource to analyze from the context}
     */
    ResourceLocation getResourceToAnalyze(C context);

    /**
     * Modifies the analysis resource according to a command, performing any additional changes as required.
     *
     * @param player the player analyzing the resource
     * @param registries the registries of the game
     * @param analyzed the analyzed resource
     * @param unlocking {@code true} if the resource is being unlocked, {@code false} if locked
     */
    void modifyFromCommand(Player player, HolderLookup.Provider registries, ResourceLocation analyzed, boolean unlocking);

    /**
     * @param registries the registries of the game.
     * {@return all analyzable resources for the analysis for display during a command}
     */
    Stream<ResourceLocation> allAnalyzableResources(HolderLookup.Provider registries);

    /**
     * Constructs a resource given the context of the analysis.
     *
     * @param context the context being analyzed
     * @return the constructed resource
     */
    default Resource<C, Analysis<C>> with(C context) {
        return new Resource<>(this, context);
    }

    record Resource<C extends AnalysisContext, A extends Analysis<C>>(A analysis, C context) {

        public boolean verifyResourceConditions() {
            return this.context.validate();
        }

        public boolean canAnalyze() {
            return this.analysis.canAnalyze(this.context);
        }

        public ResourceLocation getResourceToAnalyze() {
            return this.analysis.getResourceToAnalyze(this.context);
        }

        public void unlock() {
            this.analysis.unlock(this.context);
        }

        public Player player() {
            return this.context.player().get();
        }
    }
}
