/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client.screen;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.screen.widget.SudokuGridWidget;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.BoxConstraint;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.ColumnConstraint;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.RowConstraint;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.SudokuConstraint;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGrid;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;

import java.util.List;

// TODO: Document
public class SudokuScreen extends Screen {

    private static final SudokuGrid DUMMY = new SudokuGrid(
            Holder.direct(
                    new SudokuGridSettings(9, List.of(
                            new SudokuGridSettings.InitialValue(1, 4, '2'),
                            new SudokuGridSettings.InitialValue(1, 5, '6'),
                            new SudokuGridSettings.InitialValue(1, 7, '7'),
                            new SudokuGridSettings.InitialValue(1, 9, '1'),

                            new SudokuGridSettings.InitialValue(2, 1, '6'),
                            new SudokuGridSettings.InitialValue(2, 2, '8'),
                            new SudokuGridSettings.InitialValue(2, 5, '7'),
                            new SudokuGridSettings.InitialValue(2, 8, '9'),

                            new SudokuGridSettings.InitialValue(3, 1, '1'),
                            new SudokuGridSettings.InitialValue(3, 2, '9'),
                            new SudokuGridSettings.InitialValue(3, 6, '4'),
                            new SudokuGridSettings.InitialValue(3, 7, '5'),

                            new SudokuGridSettings.InitialValue(4, 1, '8'),
                            new SudokuGridSettings.InitialValue(4, 2, '2'),
                            new SudokuGridSettings.InitialValue(4, 4, '1'),
                            new SudokuGridSettings.InitialValue(4, 8, '4'),

                            new SudokuGridSettings.InitialValue(5, 3, '4'),
                            new SudokuGridSettings.InitialValue(5, 4, '6'),
                            new SudokuGridSettings.InitialValue(5, 6, '2'),
                            new SudokuGridSettings.InitialValue(5, 7, '9'),

                            new SudokuGridSettings.InitialValue(6, 2, '5'),
                            new SudokuGridSettings.InitialValue(6, 6, '3'),
                            new SudokuGridSettings.InitialValue(6, 8, '2'),
                            new SudokuGridSettings.InitialValue(6, 9, '8'),

                            new SudokuGridSettings.InitialValue(7, 3, '9'),
                            new SudokuGridSettings.InitialValue(7, 4, '3'),
                            new SudokuGridSettings.InitialValue(7, 8, '7'),
                            new SudokuGridSettings.InitialValue(7, 9, '4'),

                            new SudokuGridSettings.InitialValue(8, 2, '4'),
                            new SudokuGridSettings.InitialValue(8, 5, '5'),
                            new SudokuGridSettings.InitialValue(8, 8, '3'),
                            new SudokuGridSettings.InitialValue(8, 9, '6'),

                            new SudokuGridSettings.InitialValue(9, 1, '7'),
                            new SudokuGridSettings.InitialValue(9, 3, '3'),
                            new SudokuGridSettings.InitialValue(9, 5, '1'),
                            new SudokuGridSettings.InitialValue(9, 6, '8')
                    ), HolderSet.direct(
                            Holder.direct(RowConstraint.INSTANCE),
                            Holder.direct(ColumnConstraint.INSTANCE),
                            Holder.direct(new BoxConstraint(3, 3))
                    ))
            )
    );

    public SudokuScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new SudokuGridWidget(this.font, DUMMY, this.width / 2, this.height / 2, 16, 1, 1f));
    }
}
