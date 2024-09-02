package net.ashwork.mc.ashsworkshop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.command.argument.AnalysisArgument;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.network.server.ClientboundUpdateAnalyzedResources;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The base command for {@code /ashs_workshop}.
 */
public class WorkshopCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("ashs_workshop")
                        // Creative mode
                        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(AnalyzeCommand.register(context))
        );
    }

    static String translationKey(String commandName, boolean success) {
        return "commands.ashs_workshop." + commandName + "." + (success ? "success" : "failed");
    }
}
