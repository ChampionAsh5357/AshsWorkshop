package net.ashwork.mc.ashsworkshop.experimental.attribution;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.SudokuConstraint;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFileCodec;

import java.net.URI;
import java.util.Optional;

public record AttributionInfo(Component title, String author, Optional<Component> description, Optional<URI> url, Optional<Holder<License>> license) {

    public static final Codec<AttributionInfo> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("title").forGetter(AttributionInfo::title),
                    Codec.STRING.fieldOf("author").forGetter(AttributionInfo::author),
                    ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(AttributionInfo::description),
                    WorkshopCodecs.URI_LINK.optionalFieldOf("url").forGetter(AttributionInfo::url),
                    License.CODEC.optionalFieldOf("license").forGetter(AttributionInfo::license)
            ).apply(instance, AttributionInfo::new)
    );

    public record License(String name, Optional<String> spdx, Optional<URI> url) {

        public static final Codec<License> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(License::name),
                        Codec.STRING.optionalFieldOf("spdx").forGetter(License::spdx),
                        WorkshopCodecs.URI_LINK.optionalFieldOf("url").forGetter(License::url)
                ).apply(instance, License::new)
        );
        public static final Codec<Holder<License>> CODEC = RegistryFileCodec.create(ExperimentalWorkshopRegistries.LICENSE_KEY, DIRECT_CODEC);
    }
}
