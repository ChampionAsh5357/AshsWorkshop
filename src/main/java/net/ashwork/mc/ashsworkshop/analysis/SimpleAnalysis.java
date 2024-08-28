package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;

/**
 * A simple analysis that only unlocks information with any special qualities.
 */
public class SimpleAnalysis implements Analysis<AnalysisContext> {

    @Override
    public void unlock(Player player, AnalysisContext context) {
        if (context.level().isClientSide) {
            // Spawn particles on the client
            var pos = context.position();
            ParticleUtils.spawnParticlesOnBlockFaces(
                    context.level(), new BlockPos((int) pos.x, (int) pos.y, (int) pos.z),
                    ParticleTypes.EGG_CRACK, UniformInt.of(3, 6)
            );
        }
    }
}
