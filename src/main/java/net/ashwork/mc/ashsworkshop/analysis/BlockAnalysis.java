package net.ashwork.mc.ashsworkshop.analysis;

import net.ashwork.mc.ashsworkshop.init.AnalysisRegistrar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;
import java.util.stream.Stream;

/**
 * An analysis that is applied to a block.
 */
public class BlockAnalysis implements Analysis<BlockAnalysis.Context> {

    @Override
    public void unlock(Context context) {
        if (context.level().isClientSide) {
            ParticleUtils.spawnParticlesOnBlockFaces(
                    context.level(), context.pos(),
                    ParticleTypes.EGG_CRACK, UniformInt.of(3, 6)
            );
        }
    }

    @Override
    public ResourceLocation getResourceToAnalyze(Context context) {
        return context.state().getBlock().builtInRegistryHolder().key().location();
    }

    // Nothing here would only play the particle effects
    @Override
    public void modifyFromCommand(Player player, HolderLookup.Provider registries, ResourceLocation analyzed, boolean unlocking) {}

    @Override
    public Stream<ResourceLocation> allAnalyzableResources(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(Registries.BLOCK).listElements().filter(block -> block.value() instanceof BlockAnalysis.Analyzable).map(block -> block.key().location());
    }

    /**
     * Context for a block being analyzed.
     *
     * @param player the player analyzing the block
     * @param level the level the block is in
     * @param pos the position of the block
     * @param state the state of the block
     */
    public record Context(WeakReference<Player> player, Level level, BlockPos pos, BlockState state) implements AnalysisContext {

        public Context(Player player, Level level, BlockPos pos, BlockState state) {
            this(new WeakReference<>(player), level, pos, state);
        }

        @Override
        public boolean validate() {
            return AnalysisContext.super.validate()
                    && this.player.get().position().closerThan(this.position(), this.player.get().blockInteractionRange())
                    && this.level.getBlockState(this.pos) == this.state;
        }

        @Override
        public Level level() {
            return this.level;
        }

        @Override
        public Vec3 position() {
            return this.pos.getCenter();
        }
    }

    public interface Analyzable {

        /**
         * @param state the block state
         * @param context the context of the analyzer click
         * {@return the analysis resource for this block}
         */
        default Resource<Context, Analysis<Context>> getResource(BlockState state, UseOnContext context) {
            return AnalysisRegistrar.BLOCK.get().with(new Context(context.getPlayer(), context.getLevel(), context.getClickedPos(), state));
        }
    }
}
