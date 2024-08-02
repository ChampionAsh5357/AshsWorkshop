package net.ashwork.mc.ashsworkshop.experimental.client.screen;

import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = AshsWorkshop.ID, value = Dist.CLIENT)
public class ClientTestEvents {

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if (Minecraft.getInstance().screen == null && event.getKey() == GLFW.GLFW_KEY_P) {
            Minecraft.getInstance().setScreen(new SudokuScreen(Component.empty()));
        }
    }
}
