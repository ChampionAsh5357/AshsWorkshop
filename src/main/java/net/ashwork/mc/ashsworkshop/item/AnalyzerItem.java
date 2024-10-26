package net.ashwork.mc.ashsworkshop.item;

import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.analysis.AnalysisContext;
import net.ashwork.mc.ashsworkshop.analysis.AnalysisHolder;
import net.ashwork.mc.ashsworkshop.analysis.BlockAnalysis;
import net.ashwork.mc.ashsworkshop.game.sudoku.analysis.SudokuAnalysis;
import net.ashwork.mc.ashsworkshop.init.AnalysisRegistrar;
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

        if (context.getPlayer() == null) {
            return super.useOn(context);
        }

        var holder = context.getPlayer().getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);

        // Check analyzable blocks
        if (state.getBlock() instanceof BlockAnalysis.Analyzable analyzable) {
            return this.analyze(context, holder, analyzable.getResource(state, context));
        }

        // Check for sudoku lock
        var container = context.getLevel().getBlockEntity(context.getClickedPos());
        if (container != null && container.hasData(AttachmentTypeRegistrar.SUDOKU_LOCK)) {
            return this.analyze(context, holder, AnalysisRegistrar.SUDOKU.get().with(
                    new SudokuAnalysis.Context(context.getPlayer(), container.getData(AttachmentTypeRegistrar.SUDOKU_LOCK).settings())
            ));
        }

        return super.useOn(context);
    }

    private <C extends AnalysisContext> InteractionResult analyze(UseOnContext ctx, AnalysisHolder holder, Analysis.Resource<C, Analysis<C>> resource) {
        if (holder.analyze(resource)) {
            ctx.getPlayer().startUsingItem(ctx.getHand());
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
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
