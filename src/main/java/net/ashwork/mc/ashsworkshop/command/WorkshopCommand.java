package net.ashwork.mc.ashsworkshop.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.ashwork.mc.ashsworkshop.analysis.Analysis;
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

public class WorkshopCommand {

    public static final String ANALYZE_ADD_SUCCESS = analyzeKey("add", true);
    public static final String ANALYZE_ADD_FAILED_NONE = analyzeKey("add", false) + ".noanalysis";
    public static final String ANALYZE_ADD_FAILED_EXISTS = analyzeKey("add", false) + ".alreadyexists";
    public static final String ANALYZE_CLEAR_SUCCESS = analyzeKey("clear", true);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("ashs_workshop")
                        // Creative mode
                        .requires(source -> source.hasPermission(2))
                        .then(analyzeCommand(context))
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> analyzeCommand(CommandBuildContext context) {
        return Commands.literal("analyze")
                .then(
                        Commands.literal("add")
                                .then(
                                        Commands.argument("target", EntityArgument.player())
                                                .then(
                                                        Commands.argument("analysis", ResourceOrTagArgument.resourceOrTag(context, WorkshopRegistries.ANALYSIS_KEY))
                                                                .executes(
                                                                        ctx -> addAnalysis(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), ResourceOrTagArgument.getResourceOrTag(ctx, "analysis", WorkshopRegistries.ANALYSIS_KEY))
                                                                )
                                                )
                                )
                )
                // TODO: Properly implement
//                .then(
//                        Commands.literal("remove")
//                )
                .then(
                        Commands.literal("clear")
                                .then(
                                        Commands.argument("target", EntityArgument.player())
                                                .executes(
                                                        ctx -> clearAnalyses(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"))
                                                )
                                )
                );
    }

    private static int addAnalysis(CommandSourceStack source, ServerPlayer player, ResourceOrTagArgument.Result<Analysis<?>> analysis) {
        Set<ResourceLocation> analyzed = analysis.unwrap().map(
                holder -> Set.of(holder.key().location()),
                tag -> tag.stream().flatMap(holder -> holder.unwrapKey().map(ResourceKey::location).stream()).collect(Collectors.toSet())
        );

        if (analyzed.isEmpty()) {
            source.sendFailure(Component.translatable(ANALYZE_ADD_FAILED_NONE));
            return 0;
        }

        var holder = player.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);
        if (!holder.unlockResources(analyzed)) {
            source.sendFailure(Component.translatable(ANALYZE_ADD_FAILED_EXISTS, analyzed.toString()));
            return 0;
        }
        PacketDistributor.sendToPlayer(player, new ClientboundUpdateAnalyzedResources(analyzed));

        source.sendSuccess(
                () -> Component.translatable(ANALYZE_ADD_SUCCESS, player.getDisplayName(), analyzed.toString()),
                true
        );
        return 1;
    }

    private static int removeAnalysis(CommandSourceStack source, ServerPlayer player, ResourceOrTagArgument.Result<Analysis<?>> analysis) {
        // TODO: Implement
        return 0;
    }

    private static int clearAnalyses(CommandSourceStack source, ServerPlayer player) {
        var holder = player.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);
        holder.clear();
        PacketDistributor.sendToPlayer(player, new ClientboundUpdateAnalyzedResources(holder.analyzedResources(), true));

        source.sendSuccess(
                () -> Component.translatable(ANALYZE_CLEAR_SUCCESS, player.getDisplayName()),
                true
        );
        return 1;
    }

    private static String analyzeKey(String subCommand, boolean success) {
        return translationKey("analyze." + subCommand, success);
    }

    private static String translationKey(String commandName, boolean success) {
        return "commands.ashs_workshop." + commandName + "." + (success ? "success" : "failed");
    }
}
