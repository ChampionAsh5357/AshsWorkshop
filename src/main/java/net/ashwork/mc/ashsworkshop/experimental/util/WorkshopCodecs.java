package net.ashwork.mc.ashsworkshop.experimental.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

// TODO: Document, implement
public interface WorkshopCodecs {

    Codec<Character> CHAR_FROM_INT = Codec.intRange(0, 9).xmap(i -> Character.forDigit(i, 10), c -> Character.digit(c, 10));
    Codec<Character> CHAR_FROM_STRING = Codec.STRING.xmap(str -> str.charAt(0), String::valueOf);
    Codec<Character> SUDOKU_VALUE = new AlternativeCodec<>(CHAR_FROM_INT, CHAR_FROM_STRING);

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
