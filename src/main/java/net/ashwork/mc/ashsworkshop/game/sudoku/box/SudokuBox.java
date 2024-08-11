package net.ashwork.mc.ashsworkshop.game.sudoku.box;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.init.WorkshopRegistries;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SudokuBox {

    public static final Codec<SudokuBox> CODEC = WorkshopRegistries.SUDOKU_MARKING_TYPE.byNameCodec()
            .dispatch(SudokuMarking::type, type -> (MapCodec<SudokuMarking>) type.codec())
            .listOf().xmap(SudokuBox::new, SudokuBox::getMarkings);

    private final Map<SudokuMarking.Type<?>, SudokuMarking> markings;
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

    public SudokuBox(List<SudokuMarking> markings) {
        this.markings = markings.stream().collect(Collectors.toMap(SudokuMarking::type, Function.identity()));
        this.locked = false;
    }

    private List<SudokuMarking> getMarkings() {
        // Make sure the marking isn't locked or that the marking is locked but does not contain the main marking
        return this.markings.values().stream().filter(marking -> !this.isLocked() || marking.type() != MarkingRegistrar.MAIN.get())
                .filter(SudokuMarking::containsData).collect(Collectors.toList());
    }

    @Nullable
    public Character mainValue() {
        var marking = this.getMarking(MarkingRegistrar.MAIN.get());
        return marking.getValue();
    }

    public <T extends SudokuMarking> void mark(SudokuMarking.Type<T> type, char value) {
        this.getMarking(type).mark(value);
    }

    public <T extends SudokuMarking> boolean clear(SudokuMarking.Type<T> type) {
        return this.getMarking(type).clear();
    }

    public void clearAll() {
        this.markings.values().forEach(SudokuMarking::clear);
    }

    @SuppressWarnings("unchecked")
    public <T extends SudokuMarking> T getMarking(SudokuMarking.Type<T> type) {
        return (T) markings.computeIfAbsent(type, t -> t.factory().get());
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
