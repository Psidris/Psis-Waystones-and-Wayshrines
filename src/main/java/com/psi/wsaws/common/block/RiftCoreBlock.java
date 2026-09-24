package com.psi.wsaws.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RiftCoreBlock extends HorizontalDirectionalBlock {

	public RiftCoreBlock(BlockSetType stone, Properties properties) {
		super(properties);
	    this.registerDefaultState(
	    		this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> state) {
		state.add(FACING);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		// TODO Auto-generated method stub
		return RenderShape.MODEL;
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		// TODO Auto-generated method stub
		return null;
	}

}
