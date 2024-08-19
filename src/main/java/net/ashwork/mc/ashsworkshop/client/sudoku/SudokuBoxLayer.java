/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.client.sudoku;

/**
 * A set of layers that define how the sudoku box is rendered.
 */
public enum SudokuBoxLayer {
    // White background and transparent layer color (color marking)
    /**
     * The background color of the box. May be changed due to markings
     * or invalid values.
     */
    BACKGROUND,
    // Aesthetics (constraints as well)
    /**
     * The aesthetics of the board, from constraints to visual changes.
     */
    AESTHETICS,
    // Fog of war
    /**
     * A fog that covers the background. Any necessary tints and invalid values
     * are also applied here.
     */
    FOG_OF_WAR,
    // Selection
    /**
     * Whether an individual box is focused or not.
     */
    SELECTION,
    // Main value
    /**
     * The main value assumed for the sudoku box.
     */
    MAIN_VALUE,
    // Markings if main value isn't set
    /**
     * Markings if no main value is set for the box.
     */
    MARKINGS
}
