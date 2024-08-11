package net.ashwork.mc.ashsworkshop.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;

import java.util.List;

public class CenterMarkings extends AbstractMultiMarkings<Character> {

    public static final MapCodec<CenterMarkings> CODEC = multiMarkingCodec(WorkshopCodecs.SUDOKU_VALUE, CenterMarkings::new, CenterMarkings::new);

    public CenterMarkings() {
        super();
    }

    public CenterMarkings(List<Character> values) {
        super(values);
    }

    @Override
    public Type<?> type() {
        return MarkingRegistrar.CENTER.get();
    }

    @Override
    protected Character toType(char value) {
        return value;
    }
}
