package net.ashwork.mc.ashsworkshop.network;

import net.ashwork.mc.ashsworkshop.client.ClientPayloadHandler;
import net.ashwork.mc.ashsworkshop.network.server.ClientboundUpdateAnalyzedResources;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkPayloads {

    public static void register(PayloadRegistrar registrar) {
        registrar.playToClient(
                ClientboundUpdateAnalyzedResources.TYPE,
                ClientboundUpdateAnalyzedResources.STREAM_CODEC,
                new WrappedPayloadHandler<>(() -> ClientPayloadHandler::onUpdateAnalyzedResources)
        );
    }
}
