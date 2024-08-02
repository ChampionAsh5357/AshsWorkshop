package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrarAlpha1;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CenterMarkings extends AbstractMultiMarkings {

    public static final MapCodec<CenterMarkings> CODEC = codec(CenterMarkings::new);

    public CenterMarkings() {
        super();
    }

    private CenterMarkings(Optional<Set<Character>> values) {
        super(values);
    }

    @Override
    public Type type() {
        return MarkingRegistrarAlpha1.CENTERS.value();
    }

    @Override
    public SudokuMarking copy() {
        return new CenterMarkings(Optional.of(new HashSet<>(this.getValues())));
    }
}
