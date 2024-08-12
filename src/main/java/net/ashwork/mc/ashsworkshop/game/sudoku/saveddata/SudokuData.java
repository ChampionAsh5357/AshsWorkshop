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
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SudokuData extends SavedData {

    private static final String ID = AshsWorkshop.fromMod("sudoku_grid").toString().replaceAll(":", "_");
    private static final Codec<Map<Holder<SudokuGridSettings>, SudokuInfo>> INFO_CODEC = Codec.unboundedMap(SudokuGridSettings.CODEC, SudokuInfo.CODEC);
    private static final Codec<Map<UUID, Map<Holder<SudokuGridSettings>, SudokuInfo>>> CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, INFO_CODEC);

    private final Map<UUID, Map<Holder<SudokuGridSettings>, SudokuInfo>> grids;

    public static SudokuData init(ServerLevel level) {
        return level.getServer().overworld()
                .getDataStorage().computeIfAbsent(new SavedData.Factory<>(SudokuData::new, SudokuData::new), ID);
    }

    private SudokuData(CompoundTag tag, HolderLookup.Provider registries) {
        this();
        var data = CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, registries), tag).getOrThrow();
        data.forEach((uuid, dataGrids) -> {
            var playerGrids = this.grids.computeIfAbsent(uuid, k -> new HashMap<>());
            playerGrids.putAll(dataGrids);
        });
        this.setDirty();
    }

    private SudokuData() {
        this.grids = new HashMap<>();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        return CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registries), this.grids).result().map(CompoundTag.class::cast).orElseThrow();
    }

    public void updateGrid(ServerPlayer player, SudokuGrid grid) {
        this.getPlayerGrids(player).put(grid.getSettings(), new SudokuInfo(grid));
        this.setDirty();
    }

    public Map<Holder<SudokuGridSettings>, SudokuGridSettings.SolutionState> getPlayedGrids(ServerPlayer player) {
        return this.grids.getOrDefault(player.getUUID(), Collections.emptyMap()).values().stream()
                .collect(Collectors.toMap(info -> info.grid().getSettings(), SudokuInfo::state));
    }

    public SudokuGrid getGrid(ServerPlayer player, Holder<SudokuGridSettings> settings) {
        return this.grids.get(player.getUUID()).get(settings).grid();
    }

    private Map<Holder<SudokuGridSettings>, SudokuInfo> getPlayerGrids(ServerPlayer player) {
        return this.grids.computeIfAbsent(player.getUUID(), u -> new HashMap<>());
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

        public SudokuInfo(SudokuGrid grid, SudokuGridSettings.SolutionState state) {
            this.grid = grid;
            // Update state if solution has been added later
            this.state = state == SudokuGridSettings.SolutionState.FINISHED_NOT_VALIDATED
                    && this.grid.getSettings().value().hasSolution()
                    ? grid.checkSolution()
                    : state;
        }
    }
}
