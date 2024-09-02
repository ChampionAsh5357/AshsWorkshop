package net.ashwork.mc.ashsworkshop.item;

import net.ashwork.mc.ashsworkshop.analysis.BlockAnalysis;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AnalyzerItem extends Item {

    public AnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 60; // 3 seconds
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM; // Reference to AnalyzerClientItem#getArmPose and #applyForgeHandTransform
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (!(state.getBlock() instanceof BlockAnalysis.Analyzable analyzable) || context.getPlayer() == null) {
            return super.useOn(context);
        }

        var holder = context.getPlayer().getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);
        if (!holder.analyze(analyzable.getResource(state, context))) {
            // Already analyzed
            return InteractionResult.FAIL;
        }

        context.getPlayer().startUsingItem(context.getHand());
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide) {
                player.getCooldowns().addCooldown(this, 1200);
            }

            var holder = player.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);
            holder.finishAnalysis();
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        // Only stop analyzing if the player hasn't finished using the item
        if (entity instanceof Player player) {
            var holder = player.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);
            holder.stopAnalysis();
        }
        super.releaseUsing(stack, level, entity, timeCharged);
    }
}
