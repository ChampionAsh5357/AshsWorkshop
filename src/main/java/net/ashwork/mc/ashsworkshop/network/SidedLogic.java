package net.ashwork.mc.ashsworkshop.network;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A helper for handling sided logic objects.
 *
 * @param clientSide the client implementation of the logic
 * @param serverSide the server implementation of the logic
 * @param <T> the type of the logic object, should be a lambda reference
 */
public record SidedLogic<T>(Supplier<T> clientSide, Supplier<T> serverSide) implements Function<Boolean, T> {

    @Override
    public T apply(Boolean isClientSide) {
        return isClientSide ? this.clientSide.get() : this.serverSide.get();
    }
}
