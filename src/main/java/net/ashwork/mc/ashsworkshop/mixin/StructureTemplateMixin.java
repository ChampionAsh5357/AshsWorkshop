package net.ashwork.mc.ashsworkshop.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.lock.SudokuLock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    // Inject if known loot table seed
    @Inject(method = "placeInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putLong(Ljava/lang/String;J)V", shift = At.Shift.AFTER))
    private void placeInWorld(ServerLevelAccessor serverLevel, BlockPos offset, BlockPos pos, StructurePlaceSettings settings, RandomSource random, int flags, CallbackInfoReturnable<Boolean> cir, @Local StructureTemplate.StructureBlockInfo info) {
        // 5% chance of running
        if (random.nextFloat() < 0.05) {
            // Get block entity
            BlockEntity container = serverLevel.getBlockEntity(info.pos());
            if (container != null && !container.hasData(AttachmentTypeRegistrar.SUDOKU_LOCK)) {
                // Set container data if not present
                container.setData(AttachmentTypeRegistrar.SUDOKU_LOCK, new SudokuLock(
                        serverLevel.registryAccess().registryOrThrow(WorkshopRegistries.SUDOKU_GRID_KEY).getRandom(random).orElseThrow()
                ));
            }
        }
    }
}
