package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1.marking;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.experimental.init.MarkingRegistrarAlpha1;
import net.ashwork.mc.ashsworkshop.experimental.util.WorkshopCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MainMarking implements SudokuMarking {

    public static final MapCodec<MainMarking> CODEC = WorkshopCodecs.CHAR.optionalFieldOf("value").xmap(MainMarking::new, marking -> Optional.ofNullable(marking.getValue()));

    @Nullable
    private Character value;

    public MainMarking() {
        this.value = null;
    }

    private MainMarking(Optional<Character> value) {
        this.value = value.orElse(null);
    }

    @Nullable
    public Character getValue() {
        return this.value;
    }

    @Override
    public boolean hasData() {
        return this.value != null;
    }

    @Override
    public void markValue(Character value) {
        if (this.value == value) {
            this.value = null;
        } else {
            this.value = value;
        }
    }

    @Override
    public boolean clearValue() {
        if (this.value == null) {
            return false;
        }

        this.value = null;
        return true;
    }

    @Override
    public Type type() {
        return MarkingRegistrarAlpha1.MAIN.value();
    }

    @Override
    public SudokuMarking copy() {
        return new MainMarking(Optional.ofNullable(this.getValue()));
    }
}
