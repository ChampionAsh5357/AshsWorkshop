package net.ashwork.mc.ashsworkshop.init;

import com.mojang.brigadier.arguments.ArgumentType;
import net.ashwork.mc.ashsworkshop.command.argument.AnalysisArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;

import java.util.function.Supplier;

/**
 * The registrar for argument type infos.
 */
public class ArgumentTypeInfoRegistrar {

    public static final Supplier<AnalysisArgument.Info> ANALYSIS = simple("analysis", AnalysisArgument.class, AnalysisArgument.Info::new);

    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>> Supplier<I> simple(String name, Class<A> argumentClass, Supplier<I> info) {
        return WorkshopRegistrars.ARGUMENT_TYPE_INFO.register(name, () -> {
            var obj = info.get();
            ArgumentTypeInfos.registerByClass(argumentClass, obj);
            return obj;
        });
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {

    }
}
