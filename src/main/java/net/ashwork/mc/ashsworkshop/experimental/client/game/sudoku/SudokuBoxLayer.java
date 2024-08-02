package net.ashwork.mc.ashsworkshop.experimental.client.game.sudoku;

public enum SudokuBoxLayer {
    // White background and transparent layer color
    BACKGROUND,
    // If the main value is invalid
    INVALID_MAIN_VALUE_BACKGROUND,
    // Aesthetics
    AESTHETICS,
    // Fog of war
    FOG_OF_WAR,
    // Selection
    SELECTION,
    // Main value
    MAIN_VALUE,
    // Markings if main value isn't set
    MARKINGS
}
