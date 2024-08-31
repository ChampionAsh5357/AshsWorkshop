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
import java.util.stream.Collectors;

public class SudokuData extends SavedData {

    private static final String ID = AshsWorkshop.fromMod("sudoku_grid").toString().replaceAll(":", "/");
    private static final Codec<Map<Holder<SudokuGridSettings>, SudokuInfo>> CODEC = Codec.unboundedMap(SudokuGridSettings.CODEC, SudokuInfo.CODEC);

    private final Map<Holder<SudokuGridSettings>, SudokuInfo> grids;

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

    public void updateGrid(SudokuGrid grid) {
        // Assume that any grid sent back will differ
        this.grids.put(grid.getSettings(), new SudokuInfo(grid));
        this.setDirty();
    }

    public Map<Holder<SudokuGridSettings>, SudokuGridSettings.SolutionState> getPlayedGrids() {
        return this.grids.values().stream()
                .collect(Collectors.toMap(info -> info.grid().getSettings(), SudokuInfo::state));
    }

    public SudokuGrid getGrid(Holder<SudokuGridSettings> settings) {
        return this.grids.get(settings).grid();
    }

    public record SudokuInfo(SudokuGrid grid, SudokuGridSettings.SolutionState state) {

        public static final Codec<SudokuInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        SudokuGrid.CODEC.fieldOf("grid").forGetter(SudokuInfo::grid),
                        SudokuGridSettings.SolutionState.CODEC.fieldOf("state").forGetter(SudokuInfo::state)
                ).apply(instance, SudokuInfo::new)
        );

        public SudokuInfo(SudokuGrid grid) {
            this(grid, grid.checkSolution());
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
                return new SudokuInfo(this.grid, newState);
            } else {
                return this;
            }
        }
    }
}
