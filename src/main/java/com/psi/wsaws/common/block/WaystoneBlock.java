package com.psi.wsaws.common.block;

import java.util.stream.Stream;

import com.psi.wsaws.common.blockentity.WaystoneBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaystoneBlock extends BaseEntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	protected static final VoxelShape shape_lower = Shapes.join(Block.box(0, 0, 0, 16, 2, 16), Block.box(1, 0, 1, 15, 31, 15), BooleanOp.OR);
	protected static final VoxelShape shape_upper = Shapes.join(Block.box(0, -16, 0, 16, -14, 16), Block.box(1, -16, 1, 15, 15, 15), BooleanOp.OR);

	public WaystoneBlock(Properties properties) {
		super(properties);
	    this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		return new WaystoneBlockEntity(pos, state);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		if(state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			return shape_lower;
		} else return shape_upper;
	}

	@Override
	public RenderShape getRenderShape(BlockState p_60550_) {
		// TODO Auto-generated method stub
		return RenderShape.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> state) {
		state.add(FACING, HALF, POWERED);
	}
	
	

	@Override
	public BlockState updateShape(BlockState blockstate, Direction direction, BlockState newblockstate, LevelAccessor levelAccessor, BlockPos blockpos, BlockPos newblockpos) {
		DoubleBlockHalf doubleblockhalf = blockstate.getValue(HALF);
		if (direction.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
	         return newblockstate.is(this) && newblockstate.getValue(HALF) != doubleblockhalf ? blockstate.setValue(FACING, newblockstate.getValue(FACING)).setValue(POWERED, newblockstate.getValue(POWERED)) : Blocks.AIR.defaultBlockState();
	      } else {
	         return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockstate.canSurvive(levelAccessor, blockpos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockstate, direction, newblockstate, levelAccessor, blockpos, newblockpos);
	      }
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context)) {
			boolean flag = level.hasNeighborSignal(blockpos) || level.hasNeighborSignal(blockpos.above());
			return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(POWERED, Boolean.valueOf(flag));
		} else {
			return null;
		}
	}
	

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos blockpos, BlockState blockstate, Player player) {
		if(!level.isClientSide && player.isCreative()) {
			DoubleBlockHalf doubleblockhalf = blockstate.getValue(HALF);
			if (doubleblockhalf == DoubleBlockHalf.UPPER) {
				BlockPos newblockpos = blockpos.below();
				BlockState newblockstate = level.getBlockState(newblockpos);
				if (newblockstate.is(blockstate.getBlock()) && newblockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
					BlockState blockstate1 = newblockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					level.setBlock(newblockpos, blockstate1, 35);
					level.levelEvent(player, 2001, newblockpos, Block.getId(newblockstate));
				}
			}
		}
		
		super.playerWillDestroy(level, blockpos, blockstate, player);
	}

}
