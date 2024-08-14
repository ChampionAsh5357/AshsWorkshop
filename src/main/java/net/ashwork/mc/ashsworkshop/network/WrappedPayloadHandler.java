package net.ashwork.mc.ashsworkshop.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.function.Supplier;

public record WrappedPayloadHandler<T extends CustomPacketPayload>(Supplier<IPayloadHandler<T>> wrapped) implements IPayloadHandler<T> {

    @Override
    public void handle(T payload, IPayloadContext context) {
        this.wrapped.get().handle(payload, context);
    }
}
