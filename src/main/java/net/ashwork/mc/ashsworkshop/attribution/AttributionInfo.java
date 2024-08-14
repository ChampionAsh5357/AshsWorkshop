/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.attribution;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFileCodec;

import java.net.URI;
import java.util.Optional;

/**
 * Metadata for where the original source of a piece of content originated from.
 *
 * @param title the title of the content
 * @param author the author of the content
 * @param description a brief description of the content, when present
 * @param url a link to the original source of the content, when present
 * @param license the license of the content, when present
 */
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

    /**
     * The license a piece of content is provided under.
     *
     * @param name the name of the license
     * @param spdx the <a href="https://spdx.org/licenses/">SPDX identifier</a> of the license, when present
     * @param url a link to the license text, when present
     */
    public record License(String name, Optional<String> spdx, Optional<URI> url) {

        public static final Codec<License> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(License::name),
                        Codec.STRING.optionalFieldOf("spdx").forGetter(License::spdx),
                        WorkshopCodecs.URI_LINK.optionalFieldOf("url").forGetter(License::url)
                ).apply(instance, License::new)
        );
        public static final Codec<Holder<License>> CODEC = RegistryFileCodec.create(WorkshopRegistries.LICENSE_KEY, DIRECT_CODEC);
    }
}
