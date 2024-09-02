package net.ashwork.mc.ashsworkshop.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Function3;
import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.command.argument.AnalysisArgument;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.network.server.ClientboundUpdateAnalyzedResources;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.Set;

/**
 * The subcommand {@code /ashs_workshop analyze}.
 */
public class AnalyzeCommand {

    public static final String ANALYZE_FAILED_PLAYER = mainFailure("player");
    public static final String ANALYZE_ADD_FAILED_EXISTS = subFailure("add", "already_exists");
    public static final String ANALYZE_ADD_FAILED_UNLOCK = subFailure("add", "unlock");
    public static final String ANALYZE_ADD_SUCCESS = subSuccess("add");
    public static final String ANALYZE_CLEAR_SUCCESS = subSuccess("clear");
    public static final String ANALYZE_REMOVE_FAILED_NONEXISTENT = subFailure("remove", "nonexistent");
    public static final String ANALYZE_REMOVE_FAILED_LOCK = subFailure("remove", "lock");
    public static final String ANALYZE_REMOVE_SUCCESS = subSuccess("remove");

    public static LiteralArgumentBuilder<CommandSourceStack> register(CommandBuildContext context) {
        return Commands.literal("analyze")
                .then(forAnalyses(context, "add", true, AnalyzeCommand::addAnalysis))
                .then(forAnalyses(context, "remove", false, AnalyzeCommand::removeAnalysis))
                .then(Commands.literal("clear").executes(ctx -> clearAnalyses(ctx.getSource())));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> forAnalyses(CommandBuildContext context, String commandName, boolean inHolder, Function3<CommandSourceStack, Holder.Reference<Analysis<?>>, ResourceLocation, Integer> cmd) {
        var command = Commands.literal(commandName);

        context.lookupOrThrow(WorkshopRegistries.ANALYSIS_KEY).listElements().forEach(analysis -> command.then(
                Commands.literal(analysis.getRegisteredName())
                        .then(
                                Commands.argument("resource", AnalysisArgument.analysis(context, analysis, inHolder))
                                        .executes(
                                                ctx -> cmd.apply(ctx.getSource(), analysis, AnalysisArgument.getAnalysis(ctx, "resource"))
                                        )
                        )
        ));

        return command;
    }

    private static int addAnalysis(CommandSourceStack source, Holder.Reference<Analysis<?>> analysis, ResourceLocation resource) {
        // Make sure source is a player
        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable(ANALYZE_FAILED_PLAYER));
            return 0;
        }

        var sp = source.getPlayer();
        var holder = sp.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);

        // Make sure resource is not already analyzed
        if (holder.isAnalyzed(analysis.value(), resource)) {
            source.sendFailure(Component.translatable(ANALYZE_ADD_FAILED_EXISTS, analysis.key().location().toString(), resource.toString()));
            return 0;
        }

        // Unlock resource and run modify command
        try {
            analysis.value().modifyFromCommand(sp, source.registryAccess(), resource, true);
        } catch (Exception e) {
            source.sendFailure(Component.translatable(ANALYZE_ADD_FAILED_UNLOCK, analysis.key().location().toString(), resource.toString(), e.getMessage()));
            return 0;
        }
        holder.unlockResources(Map.of(analysis.value(), Set.of(resource)));
        PacketDistributor.sendToPlayer(sp, new ClientboundUpdateAnalyzedResources(analysis.value(), resource));

        source.sendSuccess(
                () -> Component.translatable(ANALYZE_ADD_SUCCESS, analysis.key().location().toString(), resource.toString()),
                true
        );
        return 1;
    }

    private static int removeAnalysis(CommandSourceStack source, Holder.Reference<Analysis<?>> analysis, ResourceLocation resource) {
        // Make sure source is a player
        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable(ANALYZE_FAILED_PLAYER));
            return 0;
        }

        var sp = source.getPlayer();
        var holder = sp.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);

        // Make sure resource is not already analyzed
        if (!holder.isAnalyzed(analysis.value(), resource)) {
            source.sendFailure(Component.translatable(ANALYZE_REMOVE_FAILED_NONEXISTENT, analysis.key().location().toString(), resource.toString()));
            return 0;
        }

        // Unlock resource and run modify command
        try {
            analysis.value().modifyFromCommand(sp, source.registryAccess(), resource, false);
        } catch (Exception e) {
            source.sendFailure(Component.translatable(ANALYZE_REMOVE_FAILED_LOCK, analysis.key().location().toString(), resource.toString(), e.getMessage()));
            return 0;
        }
        holder.lockResource(analysis.value(), resource);
        PacketDistributor.sendToPlayer(sp, new ClientboundUpdateAnalyzedResources(holder.analyzedResources(), true));

        source.sendSuccess(
                () -> Component.translatable(ANALYZE_REMOVE_SUCCESS, analysis.key().location().toString(), resource.toString()),
                true
        );
        return 1;
    }

    private static int clearAnalyses(CommandSourceStack source) {
        // Make sure source is a player
        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable(ANALYZE_FAILED_PLAYER));
            return 0;
        }

        var sp = source.getPlayer();
        var holder = sp.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER);

        // Lock all analyzed resources from command
        holder.analyzedResources().forEach((analysis, resources) ->
                resources.forEach(resource ->
                        analysis.modifyFromCommand(sp, source.registryAccess(), resource, false)
                )
        );

        // Clear holder and update
        holder.clear();
        PacketDistributor.sendToPlayer(sp, new ClientboundUpdateAnalyzedResources(holder.analyzedResources(), true));

        source.sendSuccess(
                () -> Component.translatable(ANALYZE_CLEAR_SUCCESS),
                true
        );
        return 1;
    }

    private static String subFailure(String subCommand, String reason) {
        return WorkshopCommand.translationKey("analyze." + subCommand, false) + "." + reason;
    }

    private static String subSuccess(String subCommand) {
        return WorkshopCommand.translationKey("analyze." + subCommand, true);
    }

    private static String mainFailure(String reason) {
        return WorkshopCommand.translationKey("analyze", false) + "." + reason;
    }
}
