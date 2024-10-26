package net.ashwork.mc.ashsworkshop.mixin;

import net.ashwork.mc.ashsworkshop.game.sudoku.network.common.BiboundSendPlayerGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.saveddata.SudokuData;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {

    @Shadow
    protected ResourceKey<LootTable> lootTable;

    @Inject(method = "canOpen", at = @At("HEAD"), cancellable = true)
    private void canOpen(Player player, CallbackInfoReturnable<Boolean> cir) {
        RandomizableContainerBlockEntity container = (RandomizableContainerBlockEntity) (Object) this;
        // Check if sudoku lock exists
        if (player instanceof ServerPlayer sp && this.lootTable != null && container.hasData(AttachmentTypeRegistrar.SUDOKU_LOCK)) {
            var lock = container.getData(AttachmentTypeRegistrar.SUDOKU_LOCK);
            var data = SudokuData.init(sp);
            if (lock.isUnlocked() || data.isCompleted(lock.settings())) {
                // If already completed, add to unlock
                lock.unlock();
            } else {
                // Otherwise return that the container cannot be opened
                if (!data.canView(lock.settings())) {
                    // Show message and play sound if the container has not been analyzed
                    sp.displayClientMessage(Component.literal("The container appears to have an unanalyzed lock!"), true);
                    sp.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    // Otherwise open on the client
                    PacketDistributor.sendToPlayer(sp, new BiboundSendPlayerGrid(data.getGrid(lock.settings())));
                }
                cir.setReturnValue(false);
            }
        }
    }
}
