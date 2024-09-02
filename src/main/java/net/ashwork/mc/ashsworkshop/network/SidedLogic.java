package net.ashwork.mc.ashsworkshop.network;

import net.neoforged.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

/**
 * A helper for handling sided logic objects.
 *
 * @param clientSide the client implementation of the logic
 * @param serverSide the server implementation of the logic
 * @param <T> the type of the logic object, should be a lambda reference
 */
public record SidedLogic<T>(Supplier<T> clientSide, Supplier<T> serverSide) implements Supplier<T> {

    @Override
    public T get() {
        return FMLEnvironment.dist.isClient() ? this.clientSide.get() : this.serverSide.get();
    }
}
