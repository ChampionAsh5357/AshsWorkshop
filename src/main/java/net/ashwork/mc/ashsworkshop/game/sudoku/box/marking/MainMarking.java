package net.ashwork.mc.ashsworkshop.game.sudoku.box.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.init.MarkingRegistrar;
import net.ashwork.mc.ashsworkshop.util.WorkshopCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class MainMarking extends AbstractSudokuMarking {

    public static final MapCodec<MainMarking> CODEC = WorkshopCodecs.SUDOKU_VALUE
            .optionalFieldOf("value")
            .xmap(
                    opt -> opt.map(MainMarking::new).orElseGet(MainMarking::new),
                    marking -> Optional.ofNullable(marking.getValue())
            );

    @Nullable
    private Character value;

    public MainMarking() {
        this.value = null;
    }

    private MainMarking(@Nullable Character value) {
        this.value = value;
    }

    @Nullable
    public Character getValue() {
        return this.value;
    }

    @Override
    public void mark(char value) {
        this.value = Objects.equals(this.value, value) ? null : value;
    }

    @Override
    protected void clearMark() {
        this.value = null;
    }

    @Override
    public boolean containsData() {
        return this.value != null;
    }

    @Override
    public Type<?> type() {
        return MarkingRegistrar.MAIN.get();
    }
}
