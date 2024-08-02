package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrar;

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
    public Type<?> type() {
        return MarkingRegistrar.BOX_TINT.get();
    }

    @Override
    protected Integer toType(char value) {
        return 0; // TODO: Create index map
    }
}
