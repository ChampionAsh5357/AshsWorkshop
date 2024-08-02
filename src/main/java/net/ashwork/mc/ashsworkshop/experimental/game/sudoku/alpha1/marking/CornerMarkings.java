package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrarAlpha1;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CornerMarkings extends AbstractMultiMarkings {

    public static final MapCodec<CornerMarkings> CODEC = codec(CornerMarkings::new);

    public CornerMarkings() {
        super();
    }

    private CornerMarkings(Optional<Set<Character>> values) {
        super(values);
    }

    @Override
    public Type type() {
        return MarkingRegistrarAlpha1.CORNERS.value();
    }

    @Override
    public SudokuMarking copy() {
        return new CornerMarkings(Optional.of(new HashSet<>(this.getValues())));
    }
}
