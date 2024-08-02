package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;

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
    public Type<Character, ?> type() {
        return MarkingRegistrar.CENTER.get();
    }
}
