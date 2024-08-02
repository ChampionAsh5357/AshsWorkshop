package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.SudokuMarking;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.CenterMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.CornerMarkings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking.MainMarking;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrarAlpha1;
import net.ashwork.mc.ashsworkshop.experimental.init.ExperimentalWorkshopRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: Document, implement
public class SudokuBox {

    public static final Codec<SudokuBox> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExperimentalWorkshopRegistries.SUDOKU_MARKING_TYPE_ALPHA1.byNameCodec().dispatch(SudokuMarking::type, type -> (MapCodec<SudokuMarking>) type.codec()).listOf().xmap(markings ->
                        markings.stream().collect(Collectors.toMap(SudokuMarking::type, Function.identity())), markings -> markings.values().stream().toList()
                    ).fieldOf("markings").forGetter(box -> box.markings),
                    Codec.BOOL.optionalFieldOf("locked", false).forGetter(box -> box.locked)
            ).apply(instance, SudokuBox::new)
    );

    private final Map<SudokuMarking.Type, SudokuMarking> markings;
    private final boolean locked;

    public SudokuBox() {
        this(null);
    }

    public SudokuBox(@Nullable Character initialValue) {
        this.markings = Map.of(
                MarkingRegistrarAlpha1.MAIN.value(), new MainMarking(),
                MarkingRegistrarAlpha1.CENTERS.value(), new CenterMarkings(),
                MarkingRegistrarAlpha1.CORNERS.value(), new CornerMarkings()
        );
        if (initialValue != null) {
            this.markings.get(MarkingRegistrarAlpha1.MAIN.value()).markValue(initialValue);
        }
        this.locked = initialValue != null;
    }

    private SudokuBox(Map<SudokuMarking.Type, SudokuMarking> markings, boolean locked) {
        this.markings = markings;
        this.locked = locked;

    }

    public Character value() {
        return ((MainMarking) this.markings.get(MarkingRegistrarAlpha1.MAIN.value())).getValue();
    }

    public void mark(SudokuMarking.Type type, Character value) {
        var marking = this.markings.get(type);
        if (marking != null) {
            marking.markValue(value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends SudokuMarking> T getMark(SudokuMarking.Type type) {
        return (T) this.markings.get(type);
    }

    public boolean clearMark(SudokuMarking.Type type) {
        var marking = this.markings.get(type);
        if (marking != null) {
            return marking.clearValue();
        }
        return false;
    }

    public void clearMarks() {
        this.markings.values().forEach(SudokuMarking::clearValue);
    }

    public boolean locked() {
        return this.locked;
    }

    public SudokuBox copy() {
        Map<SudokuMarking.Type, SudokuMarking> copy = this.markings.values().stream().collect(Collectors.toMap(SudokuMarking::type, SudokuMarking::copy));
        return new SudokuBox(copy, this.locked);
    }
}
