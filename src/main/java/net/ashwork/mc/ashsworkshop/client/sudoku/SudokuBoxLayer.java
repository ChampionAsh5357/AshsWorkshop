package net.ashwork.mc.ashsworkshop.client.sudoku;

public enum SudokuBoxLayer {
    // White background and transparent layer color (color marking)
    BACKGROUND,
    // If the main value is invalid
    INVALID_MAIN_VALUE_BACKGROUND,
    // Aesthetics (constraints as well)
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
