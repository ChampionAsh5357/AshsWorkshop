/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: Document, implement
public interface WorkshopCodecs {

    Codec<URI> URI_LINK = Codec.STRING.comapFlatMap(uri -> {
        try {
            return DataResult.success(Util.parseAndValidateUntrustedUri(uri));
        } catch (URISyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    }, URI::toString);

    Codec<Character> CHAR_FROM_INT = Codec.intRange(0, 9).xmap(i -> Character.forDigit(i, 10), c -> Character.digit(c, 10));
    Codec<Character> CHAR_FROM_STRING = Codec.STRING.xmap(str -> str.charAt(0), String::valueOf);
    Codec<Character> SUDOKU_VALUE = new AlternativeCodec<>(CHAR_FROM_INT, CHAR_FROM_STRING);
    StreamCodec<ByteBuf, Character> CHAR_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public Character decode(ByteBuf buffer) {
            return buffer.readChar();
        }

        @Override
        public void encode(ByteBuf buffer, Character value) {
            buffer.writeChar(value);
        }
    };

    static <T> Codec<Set<T>> set(Codec<T> elementCodec) {
        return set(elementCodec, false);
    }

    static <T> Codec<Set<T>> set(Codec<T> elementCodec, boolean skipSizeCheck) {
        return elementCodec.listOf().comapFlatMap(
                list -> {
                    Set<T> result = Set.copyOf(list);
                    return skipSizeCheck || list.size() == result.size()
                            ? DataResult.success(result)
                            : DataResult.error(() -> "Duplicate elements within the list.", result);
                }, List::copyOf
        );
    }

    static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, Set<V>> setStreamCodec() {
        return codec -> ByteBufCodecs.collection(HashSet::new, codec);
    }

    record AlternativeCodec<T>(Codec<T> codec, Codec<T> alternative) implements Codec<T> {
        @Override
        public <T1> DataResult<Pair<T, T1>> decode(final DynamicOps<T1> ops, final T1 input) {
            final DataResult<Pair<T, T1>> result = this.codec.decode(ops, input);
            return result.error().map(f -> this.alternative.decode(ops, input)).orElse(result);
        }

        @Override
        public <T1> DataResult<T1> encode(final T input, final DynamicOps<T1> ops, final T1 prefix) {
            final DataResult<T1> result = this.codec.encode(input, ops, prefix);
            return result.error().map(f -> this.alternative.encode(input, ops, prefix)).orElse(result);
        }

        @Override
        public String toString() {
            return "AlternativeCodec[" + this.codec + ", " + this.alternative + "]";
        }
    }
}
