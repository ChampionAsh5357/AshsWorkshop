/*
 * Copyright (c) ChampionAsh5357
 * SPDX-License-Identifier: MIT
 */

package net.ashwork.mc.ashsworkshop.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.ashsworkshop.analysis.BlockAnalysis;
import net.ashwork.mc.ashsworkshop.init.AnalysisRegistrar;
import net.ashwork.mc.ashsworkshop.init.AttachmentTypeRegistrar;
import net.ashwork.mc.ashsworkshop.menu.WorkbenchMenu;
import net.ashwork.mc.ashsworkshop.util.WorkshopComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * A block representing the workbench of this mod.
 */
public class WorkbenchBlock extends HorizontalDirectionalBlock implements BlockAnalysis.Analyzable {

    /**
     * A codec representing a serialized block.
     */
    public static final MapCodec<WorkbenchBlock> CODEC = simpleCodec(WorkbenchBlock::new);
    /**
     * The shape of the block in the north and south directions.
     */
    private static final VoxelShape SHAPE_NS = Shapes.or(
            Shapes.box(0.0625, 0.375, 0.125, 0.9375, 1, 0.875),
            Shapes.box(0.25, 0.1875, 0.3125, 0.75, 0.375, 0.6875),
            Shapes.box(0, 0, 0, 1, 0.1875, 1)
    );
    /**
     * The shape of the block in the east and west directions.
     */
    private static final VoxelShape SHAPE_EW = Shapes.or(
            Shapes.box(0.125, 0.375, 0.0625, 0.875, 1, 0.9375),
            Shapes.box(0.3125, 0.1875, 0.25, 0.6875, 0.375, 0.75),
            Shapes.box(0, 0, 0, 1, 0.1875, 1)
    );
    /**
     * A map of directions to shapes.
     */
    private static final ImmutableMap<Direction, VoxelShape> SHAPES = ImmutableMap.of(
            Direction.NORTH, SHAPE_NS,
            Direction.SOUTH, SHAPE_NS,
            Direction.EAST, SHAPE_EW,
            Direction.WEST, SHAPE_EW
    );

    /**
     * A simple constructor.
     *
     * @param properties the properties of the block
     */
    public WorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        // Create the workbench menu
        return new SimpleMenuProvider(
                (menuId, playerInventory, player) -> new WorkbenchMenu(menuId, playerInventory, ContainerLevelAccess.create(level, pos)),
                WorkshopComponents.WORKBENCH_MENU
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // Do not open if the object isn't analyzed
        if (!player.getData(AttachmentTypeRegistrar.ANALYSIS_HOLDER).isAnalyzed(
                AnalysisRegistrar.BLOCK.get().with(new BlockAnalysis.Context(player, level, pos, state))
        )) {
            return InteractionResult.FAIL;
        }

        // Open menu on server side
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(state.getMenuProvider(level, pos));
        }
        // SUCCESS on client, CONSUME on server
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
