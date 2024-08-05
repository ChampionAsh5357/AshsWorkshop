package net.ashwork.mc.ashsworkshop.experimental.attribution;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;

import java.net.URI;
import java.util.Optional;

public record AttributionInfo(String title, String author, Optional<String> description, Optional<URI> url, Optional<License> license) {

    public static final Codec<AttributionInfo> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("title").forGetter(AttributionInfo::title),
                    Codec.STRING.fieldOf("author").forGetter(AttributionInfo::author),
                    Codec.STRING.optionalFieldOf("description").forGetter(AttributionInfo::description),
                    WorkshopCodecs.URI_LINK.optionalFieldOf("url").forGetter(AttributionInfo::url),
                    License.CODEC.optionalFieldOf("license").forGetter(AttributionInfo::license)
            ).apply(instance, AttributionInfo::new)
    );

    record License(String name, Optional<String> spdx, Optional<URI> url) {

        public static final Codec<License> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(License::name),
                        Codec.STRING.optionalFieldOf("spdx").forGetter(License::spdx),
                        WorkshopCodecs.URI_LINK.optionalFieldOf("url").forGetter(License::url)
                ).apply(instance, License::new)
        );
    }
}
