package net.ashwork.mc.ashsworkshop.game.sudoku.saveddata;

import com.mojang.serialization.Codec;
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
import java.util.Set;
import java.util.UUID;

public class SudokuData extends SavedData {

    private static final Codec<Map<Holder<SudokuGridSettings>, SudokuGrid>> GRIDS_CODEC = Codec.unboundedMap(SudokuGridSettings.CODEC, SudokuGrid.CODEC);
    private static final Codec<Map<UUID, Map<Holder<SudokuGridSettings>, SudokuGrid>>> CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, GRIDS_CODEC);

    private final Map<UUID, Map<Holder<SudokuGridSettings>, SudokuGrid>> grids;

    public static SudokuData init(ServerLevel level) {
        return level.getServer().overworld()
                .getDataStorage().computeIfAbsent(
                        new SavedData.Factory<>(SudokuData::new, SudokuData::new),
                        AshsWorkshop.fromMod("sudoku_grid").toString()
                );
    }

    private SudokuData(CompoundTag tag, HolderLookup.Provider registries) {
        this();
        var data = CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, registries), tag).getOrThrow();
        data.forEach((uuid, dataGrids) -> {
            var playerGrids = this.grids.computeIfAbsent(uuid, k -> new HashMap<>());
            playerGrids.putAll(dataGrids);
        });
    }

    private SudokuData() {
        this.grids = new HashMap<>();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        return CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registries), this.grids).result().map(CompoundTag.class::cast).orElseThrow();
    }

    public void updateGrid(ServerPlayer player, SudokuGrid grid) {
        this.getPlayerGrids(player).put(grid.getSettings(), grid);
    }

    public Set<Holder<SudokuGridSettings>> getPlayedGrids(ServerPlayer player) {
        return this.grids.getOrDefault(player.getUUID(), Collections.emptyMap()).keySet();
    }

    public SudokuGrid getGrid(ServerPlayer player, Holder<SudokuGridSettings> settings) {
        return this.grids.get(player.getUUID()).get(settings);
    }

    private Map<Holder<SudokuGridSettings>, SudokuGrid> getPlayerGrids(ServerPlayer player) {
        return this.grids.computeIfAbsent(player.getUUID(), u -> new HashMap<>());
    }
}
