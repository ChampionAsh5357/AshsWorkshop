package net.ashwork.mc.ashsworkshop.command.argument;

import com.google.common.base.Predicates;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.ashwork.mc.ashsworkshop.analysis.Analysis;
import net.ashwork.mc.ashsworkshop.client.ClientSidedLogic;
import net.ashwork.mc.ashsworkshop.init.ArgumentTypeInfoRegistrar;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.network.SidedLogic;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An argument that represents an analyzable resource.
 */
public class AnalysisArgument implements ArgumentType<ResourceLocation> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");
    private static final SidedLogic<Function<Object, Player>> GET_PLAYER = new SidedLogic<>(
            () -> (Function<Object, Player>) ClientSidedLogic::getPlayerFromCommandSource,
            () -> (Function<Object, Player>) AnalysisArgument::getPlayer
    );

    private final HolderLookup.Provider registries;
    private final Holder.Reference<Analysis<?>> analysis;
    private final boolean inHolder;

    private AnalysisArgument(CommandBuildContext ctx, Holder.Reference<Analysis<?>> analysis, boolean inHolder) {
        this.registries = ctx;
        this.analysis = analysis;
        this.inHolder = inHolder;
    }

    public static AnalysisArgument analysis(CommandBuildContext ctx, Holder.Reference<Analysis<?>> analysis, boolean inHolder) {
        return new AnalysisArgument(ctx, analysis, inHolder);
    }

    public static ResourceLocation getAnalysis(CommandContext<CommandSourceStack> ctx, String argument) {
        return ctx.getArgument(argument, ResourceLocation.class);
    }

    @Override
    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        return ResourceLocation.read(reader);
    }

    @Nullable
    public static <S> Player getPlayer(S source) {
        if (source instanceof CommandSourceStack stack && stack.isPlayer()) {
            return stack.getPlayer();
        }

        return null;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Predicate<ResourceLocation> filter = Predicates.alwaysTrue();
        var player = GET_PLAYER.get().apply(context.getSource());
        if (player != null) {
            filter = resource -> player.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER).isAnalyzed(this.analysis.value(), resource);
            if (this.inHolder) {
                filter = filter.negate();
            }
        }

        return SharedSuggestionProvider.suggestResource(
                this.analysis.value().allAnalyzableResources(this.registries).filter(filter),
                builder
        );
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info implements ArgumentTypeInfo<AnalysisArgument, AnalysisArgument.Template> {

        @Override
        public void serializeToNetwork(AnalysisArgument.Template template, FriendlyByteBuf buffer) {
            buffer.writeResourceKey(template.analysis);
            buffer.writeBoolean(template.inHolder);
        }

        @Override
        public AnalysisArgument.Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            return new AnalysisArgument.Template(
                    buffer.readResourceKey(WorkshopRegistries.ANALYSIS_KEY),
                    buffer.readBoolean()
            );
        }

        @Override
        public void serializeToJson(AnalysisArgument.Template template, JsonObject json) {
            json.addProperty("analysis", template.analysis.location().toString());
            json.addProperty("in_holder", template.inHolder);
        }

        @Override
        public AnalysisArgument.Template unpack(AnalysisArgument argument) {
            return new AnalysisArgument.Template(argument.analysis.key(), argument.inHolder);
        }
    }

    public static class Template implements ArgumentTypeInfo.Template<AnalysisArgument> {

        final ResourceKey<Analysis<?>> analysis;
        final boolean inHolder;

        Template(ResourceKey<Analysis<?>> analysis, boolean inHolder) {
            this.analysis = analysis;
            this.inHolder = inHolder;
        }

        @Override
        public AnalysisArgument instantiate(CommandBuildContext context) {
            return new AnalysisArgument(context, context.lookupOrThrow(WorkshopRegistries.ANALYSIS_KEY).getOrThrow(this.analysis), this.inHolder);
        }

        @Override
        public ArgumentTypeInfo<AnalysisArgument, ?> type() {
            return ArgumentTypeInfoRegistrar.ANALYSIS.get();
        }
    }
}
