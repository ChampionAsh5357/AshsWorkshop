package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.box.marking;

public abstract class AbstractSudokuMarking implements SudokuMarking {

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
