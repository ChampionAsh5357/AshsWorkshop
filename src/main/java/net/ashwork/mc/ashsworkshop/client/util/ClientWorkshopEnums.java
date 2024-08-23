package net.ashwork.mc.ashsworkshop.client.util;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public interface ClientWorkshopEnums {
    EnumProxy<HumanoidModel.ArmPose> ANALYZER_PROXY = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false, (IArmPoseTransformer) ClientWorkshopEnums::analyzerTransform
    );

    private static void analyzerTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if (!entity.isUsingItem()) {
            return;
        }

        ItemStack itemInHand = entity.getItemInHand(arm == HumanoidArm.LEFT ? InteractionHand.OFF_HAND: InteractionHand.MAIN_HAND);
        float useDuration = itemInHand.getUseDuration(entity);
        float phase = 2 * (useDuration - entity.getUseItemRemainingTicks()) / useDuration;
        float offset = Mth.abs(Mth.sin((float) Math.PI * phase));

        if (arm == HumanoidArm.LEFT) {
            model.leftArm.yRot = offset * (float) Math.PI / 4;
            model.leftArm.xRot = offset * (float) -Math.PI / 4;
        } else {
            model.rightArm.yRot = offset * (float) -Math.PI / 4;
            model.rightArm.xRot = offset * (float) -Math.PI / 4;
        }
    }
}
