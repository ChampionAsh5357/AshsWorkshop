package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SudokuBox {

    public static final Codec<SudokuBox> CODEC = ExperimentalWorkshopRegistries.SUDOKU_MARKING_TYPE.byNameCodec()
            .dispatch(SudokuMarking::type, (Function<SudokuMarking.Type<?, ?>, MapCodec<? extends SudokuMarking<?>>>) SudokuMarking.Type::codec)
            .listOf().xmap(SudokuBox::new, SudokuBox::getMarkings);

    private final Map<SudokuMarking.Type<?, ?>, SudokuMarking<?>> markings;
    private final boolean locked;

    public SudokuBox() {
        this((Character) null);
    }

    public SudokuBox(@Nullable Character initialValue) {
        this.markings = new HashMap<>();
        this.locked = initialValue != null;

        if (initialValue != null) {
           this.mark(MarkingRegistrar.MAIN.get(), initialValue);
        }
    }

    public SudokuBox(List<SudokuMarking<?>> markings) {
        this.markings = markings.stream().collect(Collectors.toMap(SudokuMarking::type, Function.identity()));
        this.locked = false;
    }

    private List<SudokuMarking<?>> getMarkings() {
        // Make sure the marking isn't locked or that the marking is locked but does not contain the main marking
        return this.markings.values().stream().filter(marking -> !this.isLocked() || marking.type() != MarkingRegistrar.MAIN.get())
                .filter(SudokuMarking::containsData).collect(Collectors.toList());
    }

    @Nullable
    public Character mainValue() {
        var marking = this.getMarking(MarkingRegistrar.MAIN.get());
        return marking.getValue();
    }

    // TODO: Figure out a better solution
    @Deprecated
    public <M extends SudokuMarking<Character>> void markChar(SudokuMarking.Type<Character, M> type, Character value) {
        this.getMarking(type).mark(value);
    }

    public <T, M extends SudokuMarking<T>> void mark(SudokuMarking.Type<T, M> type, T value) {
        this.getMarking(type).mark(value);
    }

    public <T, M extends SudokuMarking<T>> boolean clear(SudokuMarking.Type<T, M> type) {
        return this.getMarking(type).clear();
    }

    public boolean clearAll() {
        boolean cleared = false;
        for (var marking : markings.values()) {
            cleared |= marking.clear();
        }

        return cleared;
    }

    @SuppressWarnings("unchecked")
    public <T, M extends SudokuMarking<T>> M getMarking(SudokuMarking.Type<T, M> type) {
        return (M) markings.computeIfAbsent(type, t -> t.factory().get());
    }

    public boolean containsData() {
        return this.markings.values().stream().filter(marking -> !this.isLocked() || marking.type() != MarkingRegistrar.MAIN.get()).anyMatch(SudokuMarking::containsData);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void mergeMarkings(SudokuBox box) {
        box.markings.forEach(this.markings::putIfAbsent);
    }
}
