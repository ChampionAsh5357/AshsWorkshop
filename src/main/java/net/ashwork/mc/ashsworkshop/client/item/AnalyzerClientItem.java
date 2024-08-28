package net.ashwork.mc.ashsworkshop.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ashwork.mc.ashsworkshop.client.util.ClientWorkshopEnums;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

/**
 * The client item extension for {@link net.ashwork.mc.ashsworkshop.item.AnalyzerItem}.
 */
public class AnalyzerClientItem implements IClientItemExtensions {

    /**
     * The enum extension for the analyzer arm pose.
     */
    private static final HumanoidModel.ArmPose ANALYZER = ClientWorkshopEnums.ANALYZER_PROXY.getValue();

    @Nullable
    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return ANALYZER;
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        // Only apply on item use
        if (player.isUsingItem()) {
            // Flip depending on arm
            int armX = arm == HumanoidArm.RIGHT ? 1 : -1;

            // Construct offset period, basic sine animation of moving up and down
            float useDuration = itemInHand.getUseDuration(player);
            float phase = 2 * (useDuration - (player.getUseItemRemainingTicks() - partialTick + 1)) / useDuration;
            float offset = 0.2F * Mth.abs(Mth.sin((float) Math.PI * phase));

            // Apply the appropriate translations
            poseStack.translate(armX * (0.56F - offset), -0.52F - offset, -0.72F - offset);
            return true;
        }
        return false;
    }
}
