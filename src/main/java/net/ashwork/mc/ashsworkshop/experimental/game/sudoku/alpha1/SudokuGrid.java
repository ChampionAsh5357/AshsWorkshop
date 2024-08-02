package net.ashwork.mc.ashsworkshop.experimental.game.sudoku.alpha1;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Document, implement
public class SudokuGrid {

    public static final Codec<SudokuGrid> CODEC = SudokuBox.CODEC.listOf().xmap(SudokuGrid::new, SudokuGrid::getBoxes);

    private final List<SudokuBox> boxes;

    public SudokuGrid(List<SudokuBox> boxes) {
        this.boxes = boxes;
    }

    public SudokuGrid() {
        this.boxes = new ArrayList<>(81);
        for (int i = 0; i < 81; i++) {
            boxes.add(new SudokuBox());
        }
    }

    public SudokuBox get(int row, int col) {
        return this.boxes.get(col * 9 + row);
    }

    public List<SudokuBox> getBoxes() {
        return this.boxes;
    }

    public static SudokuGrid constructTestGrid() {
        Map<Integer, SudokuBox> idxToBox = new HashMap<>();
        idxToBox.put(0 * 9 + 3, new SudokuBox('2'));
        idxToBox.put(0 * 9 + 4, new SudokuBox('6'));
        idxToBox.put(0 * 9 + 6, new SudokuBox('7'));
        idxToBox.put(0 * 9 + 8, new SudokuBox('1'));
        idxToBox.put(1 * 9 + 0, new SudokuBox('6'));
        idxToBox.put(1 * 9 + 1, new SudokuBox('8'));
        idxToBox.put(1 * 9 + 4, new SudokuBox('7'));
        idxToBox.put(1 * 9 + 7, new SudokuBox('9'));
        idxToBox.put(2 * 9 + 0, new SudokuBox('1'));
        idxToBox.put(2 * 9 + 1, new SudokuBox('9'));
        idxToBox.put(2 * 9 + 5, new SudokuBox('4'));
        idxToBox.put(2 * 9 + 6, new SudokuBox('5'));
        idxToBox.put(3 * 9 + 0, new SudokuBox('8'));
        idxToBox.put(3 * 9 + 1, new SudokuBox('2'));
        idxToBox.put(3 * 9 + 3, new SudokuBox('1'));
        idxToBox.put(3 * 9 + 7, new SudokuBox('4'));
        idxToBox.put(4 * 9 + 2, new SudokuBox('4'));
        idxToBox.put(4 * 9 + 3, new SudokuBox('6'));
        idxToBox.put(4 * 9 + 5, new SudokuBox('2'));
        idxToBox.put(4 * 9 + 6, new SudokuBox('9'));
        idxToBox.put(5 * 9 + 1, new SudokuBox('5'));
        idxToBox.put(5 * 9 + 5, new SudokuBox('3'));
        idxToBox.put(5 * 9 + 7, new SudokuBox('2'));
        idxToBox.put(5 * 9 + 8, new SudokuBox('8'));
        idxToBox.put(6 * 9 + 2, new SudokuBox('9'));
        idxToBox.put(6 * 9 + 3, new SudokuBox('3'));
        idxToBox.put(6 * 9 + 7, new SudokuBox('7'));
        idxToBox.put(6 * 9 + 8, new SudokuBox('4'));
        idxToBox.put(7 * 9 + 1, new SudokuBox('4'));
        idxToBox.put(7 * 9 + 4, new SudokuBox('5'));
        idxToBox.put(7 * 9 + 7, new SudokuBox('3'));
        idxToBox.put(7 * 9 + 8, new SudokuBox('6'));
        idxToBox.put(8 * 9 + 0, new SudokuBox('7'));
        idxToBox.put(8 * 9 + 2, new SudokuBox('3'));
        idxToBox.put(8 * 9 + 4, new SudokuBox('1'));
        idxToBox.put(8 * 9 + 5, new SudokuBox('8'));

        List<SudokuBox> boxes = new ArrayList<>(81);
        for (int i = 0; i < 81; i++) {
            var box = idxToBox.get(i);
            boxes.add(box != null ? box : new SudokuBox());
        }
        return new SudokuGrid(boxes);
    }
}
