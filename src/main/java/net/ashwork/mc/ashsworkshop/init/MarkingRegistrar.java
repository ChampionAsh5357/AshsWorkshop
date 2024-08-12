package net.ashwork.mc.ashsworkshop.init;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.BoxTintMarkings;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.CenterMarkings;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.CornerMarkings;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.MainMarking;
import net.ashwork.mc.ashsworkshop.game.sudoku.box.marking.SudokuMarking;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class MarkingRegistrar {

    public static final Supplier<SudokuMarking.Type<MainMarking>> MAIN = registerType("main", MainMarking::new, MainMarking.CODEC, MainMarking.STREAM_CODEC);
    public static final Supplier<SudokuMarking.Type<CenterMarkings>> CENTER = registerType("center", CenterMarkings::new, CenterMarkings.CODEC, CenterMarkings.STREAM_CODEC);
    public static final Supplier<SudokuMarking.Type<CornerMarkings>> CORNER = registerType("corner", CornerMarkings::new, CornerMarkings.CODEC, CornerMarkings.STREAM_CODEC);
    public static final Supplier<SudokuMarking.Type<BoxTintMarkings>> BOX_TINT = registerType("box_tint", BoxTintMarkings::new, BoxTintMarkings.CODEC, BoxTintMarkings.STREAM_CODEC);

    private static <T extends SudokuMarking> Supplier<SudokuMarking.Type<T>> registerType(String name, Supplier<T> factory, MapCodec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {
        return WorkshopRegistrars.SUDOKU_MARKING_TYPE.register(name, () -> new SudokuMarking.Type<>(factory, codec, streamCodec));
    }

    /**
     * Loads the registrar class and registers all registry objects.
     */
    public static void register() {}
}
