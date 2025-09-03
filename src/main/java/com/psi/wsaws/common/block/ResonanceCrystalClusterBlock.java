package com.psi.wsaws.common.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ResonanceCrystalClusterBlock extends ResonanceCrystalBlock implements SimpleWaterloggedBlock {
	   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	   public static final DirectionProperty FACING = BlockStateProperties.FACING;
	   protected final VoxelShape northAabb;
	   protected final VoxelShape southAabb;
	   protected final VoxelShape eastAabb;
	   protected final VoxelShape westAabb;
	   protected final VoxelShape upAabb;
	   protected final VoxelShape downAabb;

	public ResonanceCrystalClusterBlock(int offsetX, int offsetY, Properties properties) {
		super(properties);
	      this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.UP));
	      this.upAabb = Block.box((double)offsetY, 0.0D, (double)offsetY, (double)(16 - offsetY), (double)offsetX, (double)(16 - offsetY));
	      this.downAabb = Block.box((double)offsetY, (double)(16 - offsetX), (double)offsetY, (double)(16 - offsetY), 16.0D, (double)(16 - offsetY));
	      this.northAabb = Block.box((double)offsetY, (double)offsetY, (double)(16 - offsetX), (double)(16 - offsetY), (double)(16 - offsetY), 16.0D);
	      this.southAabb = Block.box((double)offsetY, (double)offsetY, 0.0D, (double)(16 - offsetY), (double)(16 - offsetY), (double)offsetX);
	      this.eastAabb = Block.box(0.0D, (double)offsetY, (double)offsetY, (double)offsetX, (double)(16 - offsetY), (double)(16 - offsetY));
	      this.westAabb = Block.box((double)(16 - offsetX), (double)offsetY, (double)offsetY, 16.0D, (double)(16 - offsetY), (double)(16 - offsetY));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		Direction direction = state.getValue(FACING);
		switch (direction) {
		case NORTH:
			return this.northAabb;
		case SOUTH:
			return this.southAabb;
		case EAST:
			return this.eastAabb;
		case WEST:
			return this.westAabb;
		case DOWN:
			return this.downAabb;
		case UP:
		default:
			return this.upAabb;
		}
	}

	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction.getOpposite());
		return reader.getBlockState(blockpos).isFaceSturdy(reader, blockpos, direction);
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState newstate, LevelAccessor accessor, BlockPos pos, BlockPos newpos) {
		if (state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(accessor, pos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, direction, newstate, accessor, pos, newpos);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		LevelAccessor levelaccessor = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		return this.defaultBlockState()
				.setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER))
				.setValue(FACING, context.getClickedFace());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror miror) {
		return state.rotate(miror.getRotation(state.getValue(FACING)));
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(WATERLOGGED, FACING);
	}
}