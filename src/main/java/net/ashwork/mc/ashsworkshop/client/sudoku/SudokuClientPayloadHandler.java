package net.ashwork.mc.ashsworkshop.client.sudoku;

import net.ashwork.mc.ashsworkshop.client.screen.WorkbenchScreen;
import net.ashwork.mc.ashsworkshop.client.sudoku.screen.SudokuSelectionScreen;
import net.ashwork.mc.ashsworkshop.game.sudoku.network.server.ClientboundSendPlayerGrids;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SudokuClientPayloadHandler {

    public static void onRequestPlayerGrids(ClientboundSendPlayerGrids payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            boolean fullscreen = Minecraft.getInstance().screen instanceof WorkbenchScreen workbench && workbench.isFullscreen();
            Minecraft.getInstance().setScreen(new SudokuSelectionScreen(Component.empty(), payload.settings(), fullscreen));
        });
    }
}
