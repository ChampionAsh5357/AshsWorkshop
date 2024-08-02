package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking;

public abstract class AbstractSudokuMarking<T> implements SudokuMarking<T> {

    @Override
    public boolean clear() {
        if (!this.containsData()) {
            return false;
        }

        this.clearMark();
        return true;
    }

    protected abstract void clearMark();
}
