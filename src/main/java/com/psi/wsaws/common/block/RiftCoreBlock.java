package com.psi.wsaws.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RiftCoreBlock extends HorizontalDirectionalBlock {
	protected static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 16, 13);

	public RiftCoreBlock(BlockSetType stone, Properties properties) {
		super(properties);
	    this.registerDefaultState(
	    		this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> state) {
		state.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return null;
	}

}
