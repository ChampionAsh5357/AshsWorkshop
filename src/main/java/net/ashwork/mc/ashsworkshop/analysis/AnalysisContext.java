package net.ashwork.mc.ashsworkshop.analysis;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.lang.ref.WeakReference;

/**
 * The context of the analysis.
 */
public interface AnalysisContext {

    /**
     * {@return true when the context has remained the same on final usage}
     */
    default boolean validate() {
        return this.player().get() != null;
    }

    /**
     * {@return the player performing the analysis}
     */
    WeakReference<Player> player();

    /**
     * {@return the current level}
     */
    default Level level() {
        return this.player().get().level();
    }

    /**
     * {@return the position of the element being analyzed}
     */
    default Vec3 position() {
        return this.player().get().position();
    }
}
