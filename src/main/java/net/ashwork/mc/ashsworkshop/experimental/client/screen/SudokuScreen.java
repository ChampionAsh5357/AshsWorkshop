/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.experimental.client.screen;

import net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku.screen.widget.SudokuGridWidget;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.BoxConstraint;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.ColumnConstraint;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.constraint.RowConstraint;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGridSettings;
import net.ashwork.mc.ashsworkshop.experimental.game.sudoku.grid.SudokuGrid;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO: Document
public class SudokuScreen extends Screen {

    private static final SudokuGrid DUMMY_9x9 = new SudokuGrid(
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
                    ), Optional.empty())
            )
    );

    private static final SudokuGrid DUMMY_6x6 = new SudokuGrid(
            Holder.direct(
                    new SudokuGridSettings(6, List.of(
                            new SudokuGridSettings.InitialValue(1, 3, '5'),

                            new SudokuGridSettings.InitialValue(2, 2, '1'),
                            new SudokuGridSettings.InitialValue(2, 6, '5'),

                            new SudokuGridSettings.InitialValue(3, 3, '3'),
                            new SudokuGridSettings.InitialValue(3, 5, '2'),
                            new SudokuGridSettings.InitialValue(3, 6, '6'),

                            new SudokuGridSettings.InitialValue(4, 1, '2'),
                            new SudokuGridSettings.InitialValue(4, 2, '6'),
                            new SudokuGridSettings.InitialValue(4, 4, '3'),

                            new SudokuGridSettings.InitialValue(5, 1, '6'),
                            new SudokuGridSettings.InitialValue(5, 5, '4'),

                            new SudokuGridSettings.InitialValue(6, 4, '6')
                    ), HolderSet.direct(
                            Holder.direct(RowConstraint.INSTANCE),
                            Holder.direct(ColumnConstraint.INSTANCE),
                            Holder.direct(new BoxConstraint(2, 3))
                    ), Optional.empty())
            )
    );

    private static final SudokuGrid DUMMY_4x4 = new SudokuGrid(
            Holder.direct(
                    new SudokuGridSettings(4, List.of(
                            new SudokuGridSettings.InitialValue(2, 1, '4'),
                            new SudokuGridSettings.InitialValue(2, 2, '2'),
                            new SudokuGridSettings.InitialValue(2, 4, '1'),

                            new SudokuGridSettings.InitialValue(3, 1, '1'),
                            new SudokuGridSettings.InitialValue(3, 3, '2'),
                            new SudokuGridSettings.InitialValue(3, 4, '4')
                    ), HolderSet.direct(
                            Holder.direct(RowConstraint.INSTANCE),
                            Holder.direct(ColumnConstraint.INSTANCE),
                            Holder.direct(new BoxConstraint(2, 2))
                    ), Optional.empty())
            )
    );

    private final List<SudokuGridWidget> dummyGrids = new ArrayList<>();
    private int idx = 0;

    public SudokuScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        this.dummyGrids.add(this.addRenderableWidget(new SudokuGridWidget(this.font, DUMMY_9x9, this.width / 2, this.height / 2, 16, 1, 1f)));
        var grid6x6 = this.addRenderableWidget(new SudokuGridWidget(this.font, DUMMY_6x6, this.width / 2, this.height / 2, 16, 1, 1f));
        grid6x6.visible = false;
        this.dummyGrids.add(grid6x6);
        var grid4x4 = this.addRenderableWidget(new SudokuGridWidget(this.font, DUMMY_4x4, this.width / 2, this.height / 2, 16, 1, 1f));
        grid4x4.visible = false;
        this.dummyGrids.add(grid4x4);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_L) {
            this.dummyGrids.get(this.idx).visible = false;
            this.idx = ++this.idx % this.dummyGrids.size();
            this.dummyGrids.get(this.idx).visible = true;
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
