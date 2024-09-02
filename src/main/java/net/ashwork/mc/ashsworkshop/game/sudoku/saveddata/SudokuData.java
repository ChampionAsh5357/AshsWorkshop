/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.game.sudoku.saveddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.ashsworkshop.AshsWorkshop;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGrid;
import net.ashwork.mc.ashsworkshop.game.sudoku.grid.SudokuGridSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * The data of the stored sudoku grids. Each sudoku grid is stored within its own subfolder for each player.
 */
public class SudokuData extends SavedData {

    private static final String ID = AshsWorkshop.fromMod("sudoku_grid").toString().replaceAll(":", "/");
    private static final Codec<Map<Holder<SudokuGridSettings>, SudokuInfo>> CODEC = Codec.unboundedMap(SudokuGridSettings.CODEC, SudokuInfo.CODEC);

    private final Map<Holder<SudokuGridSettings>, SudokuInfo> grids;

    /**
     * Gets the sudoku data for that player, or creates one if it doesn't exist.
     *
     * @param player the player
     * @return the {@code SudokuData} instance
     */
    public static SudokuData init(ServerPlayer player) {
        return player.getServer().overworld()
                .getDataStorage().computeIfAbsent(new SavedData.Factory<>(SudokuData::new, SudokuData::new), getPlayerSavedData(player));
    }

    private static String getPlayerSavedData(ServerPlayer player) {
        return ID + "/" + player.getUUID();
    }

    private SudokuData(CompoundTag tag, HolderLookup.Provider registries) {
        this();
        // Put all data from the sudoku grid in
        var data = CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, registries), tag).getOrThrow();
        data.forEach((settings, info) -> this.grids.put(settings, info.recheckSolution(this::setDirty)));
    }

    private SudokuData() {
        this.grids = new HashMap<>();
    }

    @Override
    public void save(File file, HolderLookup.Provider registries) {
        if (this.isDirty()) {
            try {
                // Create missing directories
                Files.createDirectories(file.toPath().getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.save(file, registries);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        return CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registries), this.grids).result().map(CompoundTag.class::cast).orElseThrow();
    }

    /**
     * Updates the stored sudoku grid, rechecking the solution as necessary.
     *
     * @param grid the grid to update
     */
    public void updateGrid(SudokuGrid grid) {
        // Assume that any grid sent back will differ
        this.update(grid.getSettings(), (settings, info) -> (info == null) ? new SudokuInfo(grid) : info.update(grid));
    }

    /**
     * Sets whether the grid is viewable
     *
     * @param settings the settings of the grid
     * @param canView whether the grid can be viewed in the selection menu
     */
    public void setView(Holder<SudokuGridSettings> settings, boolean canView) {
        this.update(settings, (st, info) -> (info == null) ? new SudokuInfo(new SudokuGrid(st), SudokuGridSettings.SolutionState.NEW, canView) : info.view(canView));
    }

    private void update(Holder<SudokuGridSettings> settings, BiFunction<Holder<SudokuGridSettings>, SudokuInfo, SudokuInfo> compute) {
        this.grids.compute(settings, compute);
        this.setDirty();
    }

    /**
     * {@return the grids the player has started and analyzed}
     */
    public Map<Holder<SudokuGridSettings>, SudokuGridSettings.SolutionState> getPlayedGrids() {
        return this.grids.values().stream().filter(SudokuInfo::canView)
                .collect(Collectors.toMap(info -> info.grid().getSettings(), SudokuInfo::state));
    }

    /**
     * @param settings the settings of the grid
     * {@return {@code true} if the grid can be viewed, {@code false} otherwise}
     */
    public boolean canView(Holder<SudokuGridSettings> settings) {
        return !this.grids.containsKey(settings) || this.grids.get(settings).canView();
    }

    /**
     * @param settings
     * {@return the sudoku grid based on the settings}
     */
    public SudokuGrid getGrid(Holder<SudokuGridSettings> settings) {
        return this.grids.get(settings).grid();
    }

    public record SudokuInfo(SudokuGrid grid, SudokuGridSettings.SolutionState state, boolean canView) {

        public static final Codec<SudokuInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        SudokuGrid.CODEC.fieldOf("grid").forGetter(SudokuInfo::grid),
                        SudokuGridSettings.SolutionState.CODEC.fieldOf("state").forGetter(SudokuInfo::state),
                        Codec.BOOL.optionalFieldOf("can_view", false).forGetter(SudokuInfo::canView)
                ).apply(instance, SudokuInfo::new)
        );

        public SudokuInfo(SudokuGrid grid) {
            this(grid, grid.checkSolution(), false);
        }

        private SudokuInfo recheckSolution(Runnable setDirty) {
            var newState = this.state == SudokuGridSettings.SolutionState.FINISHED_NOT_VALIDATED
                    // Make sure there is a solution to validate
                    && this.grid.getSettings().value().hasSolution()
                    // Check solution
                    ? this.grid.checkSolution()
                    // Otherwise return original state
                    : this.state;

            if (newState != this.state) {
                // If state differs, set dirty and recreate info
                setDirty.run();
                return new SudokuInfo(this.grid, newState, this.canView);
            } else {
                return this;
            }
        }

        public SudokuInfo update(SudokuGrid grid) {
            return new SudokuInfo(grid, grid.checkSolution(), this.canView);
        }

        public SudokuInfo view(boolean canView) {
            return this.canView == canView ? this : new SudokuInfo(this.grid, this.state, canView);
        }
    }
}
