package net.ashwork.mc.ashsworkshop.lock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.minecraft.core.Holder;

/**
 * A lock that is applied to some openable block entity that requires the
 * sudoku grid to be completed to unlock.
 */
public class SudokuLock {

    public static final Codec<SudokuLock> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    SudokuGridSettings.CODEC.fieldOf("settings").forGetter(lock -> lock.settings),
                    Codec.BOOL.fieldOf("unlocked").forGetter(lock -> lock.unlocked)
            ).apply(instance, SudokuLock::new)
    );

    private final Holder<SudokuGridSettings> settings;
    private boolean unlocked;

    public SudokuLock(Holder<SudokuGridSettings> settings) {
        this(settings, false);
    }

    private SudokuLock(Holder<SudokuGridSettings> settings, boolean unlocked) {
        this.settings = settings;
        this.unlocked = unlocked;
    }

    public Holder<SudokuGridSettings> settings() {
        return this.settings;
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public void unlock() {
        this.unlocked = true;
    }
}
