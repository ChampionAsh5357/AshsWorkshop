package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

public abstract class AbstractMultiMarkings implements SudokuMarking {

    private final Set<Character> values;

    protected AbstractMultiMarkings() {
        this.values = new TreeSet<>(Comparator.naturalOrder());
    }

    protected AbstractMultiMarkings(Optional<Set<Character>> values) {
        this.values = values.orElseGet(() -> new TreeSet<>(Comparator.naturalOrder()));
    }

    protected static <T extends AbstractMultiMarkings> MapCodec<T> codec(Function<Optional<Set<Character>>, T> factory) {
        return WorkshopCodecs.treeSetNaturalCodec(WorkshopCodecs.CHAR)
                .optionalFieldOf("values")
                .xmap(factory, marking -> marking.getValues().isEmpty() ? Optional.empty() : Optional.of(marking.getValues()));
    }

    public Set<Character> getValues() {
        return this.values;
    }

    @Override
    public void markValue(Character value) {
        if (!this.values.add(value)) {
            this.values.remove(value);
        }
    }

    @Override
    public boolean hasData() {
        return !this.values.isEmpty();
    }

    @Override
    public boolean clearValue() {
        if (this.values.isEmpty()) {
            return false;
        }

        this.values.clear();
        return true;
    }
}
