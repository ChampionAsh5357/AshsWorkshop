package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha2.box.marking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;

import java.util.List;

public class BoxTintMarkings extends AbstractMultiMarkings<Integer> {

    public static final MapCodec<BoxTintMarkings> CODEC = multiMarkingCodec(Codec.INT, BoxTintMarkings::new, BoxTintMarkings::new);

    public BoxTintMarkings() {
        super();
    }

    public BoxTintMarkings(List<Integer> values) {
        super(values);
    }

    @Override
    public Type<Integer, ?> type() {
        return MarkingRegistrar.BOX_TINT.get();
    }
}
