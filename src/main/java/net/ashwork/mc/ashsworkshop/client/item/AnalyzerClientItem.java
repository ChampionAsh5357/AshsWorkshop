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

public class AnalyzerClientItem implements IClientItemExtensions {

    private static final HumanoidModel.ArmPose ANALYZER = ClientWorkshopEnums.ANALYZER_PROXY.getValue();

    @Nullable
    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return ANALYZER;
    }

    // TODO: Maybe add target animation?
    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        if (player.isUsingItem()) {
            int armX = arm == HumanoidArm.RIGHT ? 1 : -1;
            float useDuration = itemInHand.getUseDuration(player);
            float phase = 2 * (useDuration - (player.getUseItemRemainingTicks() - partialTick + 1)) / useDuration;
            float offset = 0.2F * Mth.abs(Mth.sin((float) Math.PI * phase));
            poseStack.translate(armX * (0.56F - offset), -0.52F - offset, -0.72F - offset);
            return true;
        }
        return false;
    }
}
