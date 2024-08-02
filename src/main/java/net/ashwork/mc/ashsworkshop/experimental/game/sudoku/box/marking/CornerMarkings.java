package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;

import java.util.List;

public class CornerMarkings extends AbstractMultiMarkings<Character> {

    public static final MapCodec<CornerMarkings> CODEC = multiMarkingCodec(WorkshopCodecs.SUDOKU_VALUE, CornerMarkings::new, CornerMarkings::new);

    public CornerMarkings() {
        super();
    }

    public CornerMarkings(List<Character> values) {
        super(values);
    }

    @Override
    public Type<?> type() {
        return MarkingRegistrar.CORNER.get();
    }

    @Override
    protected Character toType(char value) {
        return value;
    }
}
