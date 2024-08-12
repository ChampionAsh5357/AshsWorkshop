package net.ashwork.mc.ashsworkshop.game.sudoku.box.marking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractMultiMarkings<T extends Comparable<? super T>> extends AbstractSudokuMarking {

    protected static <T extends Comparable<? super T>, M extends AbstractMultiMarkings<T>> MapCodec<M> multiMarkingCodec(Codec<T> elementCodec, Function<List<T>, M> parser) {
        return elementCodec.listOf().fieldOf("values").xmap(parser, marking -> new ArrayList<>(marking.getValues()));
    }

    protected static <T extends Comparable<? super T>, M extends AbstractMultiMarkings<T>> StreamCodec<ByteBuf, M> multiMarkingStreamCodec(StreamCodec<ByteBuf, T> elementStreamCodec, Function<List<T>, M> parser) {
        return elementStreamCodec.apply(ByteBufCodecs.list()).map(parser, marking -> new ArrayList<>(marking.getValues()));
    }

    private final Set<T> values;

    protected AbstractMultiMarkings() {
        this.values = new TreeSet<>(Comparator.naturalOrder());
    }

    protected AbstractMultiMarkings(List<T> values) {
        this();
        this.values.addAll(values);
    }

    public Set<T> getValues() {
        return this.values;
    }

    @Override
    protected void clearMark() {
        this.values.clear();
    }

    protected abstract T toType(char value);

    @Override
    public void mark(char value) {
        T val = this.toType(value);
        if (!this.values.add(val)) {
            this.values.remove(val);
        }
    }

    @Override
    public boolean containsData() {
        return !this.values.isEmpty();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + this.values;
    }
}
