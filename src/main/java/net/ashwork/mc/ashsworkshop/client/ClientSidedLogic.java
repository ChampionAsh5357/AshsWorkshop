package net.ashwork.mc.ashsworkshop.client;

import net.ashwork.mc.ashsworkshop.command.argument.AnalysisArgument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.world.entity.player.Player;

/**
 * A class which holds methods for running client sided logic.
 */
public class ClientSidedLogic {

    public static <S> Player getPlayerFromCommandSource(S source) {
        if (source instanceof ClientSuggestionProvider) {
            return Minecraft.getInstance().player;
        }

        return AnalysisArgument.getPlayer(source);
    }
}
