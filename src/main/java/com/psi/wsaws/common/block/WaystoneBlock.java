package com.psi.wsaws.common.block;

import java.util.stream.Stream;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.blockentity.BlockEntityInit;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockEntity;
import com.psi.wsaws.common.util.ChunkHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaystoneBlock extends BaseEntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	protected static final VoxelShape shape_lower = Shapes.join(Block.box(0, 0, 0, 16, 2, 16), Block.box(1, 0, 1, 15, 31, 15), BooleanOp.OR);
	protected static final VoxelShape shape_upper = Block.box(5,0,5,10,10,10);
			//Shapes.join(Block.box(0, -16, 0, 16, -14, 16), Block.box(1, -16, 1, 15, 15, 15), BooleanOp.OR);
	protected static final AABB TELEPORT_OFFSET = new AABB(-1.5D, -1.5D, -1.5D, 2.5D, 2.5D, 2.5D);
	

	public WaystoneBlock(Properties properties) {
		super(properties);
	    this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get().create(pos, state);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
		if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
			if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				use(level.getBlockState(pos.below()), level, pos.below(), player, hand, blockHitResult);
				return InteractionResult.PASS;
			} else {
				if(level.getExistingBlockEntity(pos) != null) {
					//get reference to this waystone
					if(level.getExistingBlockEntity(pos) instanceof WaystoneBlockEntity waystone) {
						ItemStack item = player.getItemInHand(hand);
						if(item.is(this.asItem())) {
							CompoundTag tag = new CompoundTag();
							tag.put("linked_pos", NbtUtils.writeBlockPos(pos));
							item.setTag(tag);
							WSaWS.LOGGER.debug("Position stored to item");
							return InteractionResult.SUCCESS;
						} else {
							if(waystone.validateLink()) {
								waystone.requestTeleport((ServerPlayer) player);
								return InteractionResult.SUCCESS;
							} else {
								return InteractionResult.FAIL;
							}
						}
					}
				}
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entitytype) {
		return createTickerHelper(entitytype, BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get(), WaystoneBlockEntity::tick);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		for(int j = 0; j < 4; ++j) {
            double d0 = (double)pos.getX() + random.nextDouble();
            double d1 = (double)pos.getY() + random.nextDouble();
            double d2 = (double)pos.getZ() + random.nextDouble();
            double d3 = (random.nextDouble() - 0.5D) * 0.5D;
            double d4 = (random.nextDouble() - 0.5D) * 0.5D;
            double d5 = (random.nextDouble() - 0.5D) * 0.5D;
            int k = random.nextInt(2) * 2 - 1;
            if (random.nextBoolean()) {
               d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
               d5 = (double)(random.nextFloat() * 2.0F * (float)k);
            } else {
               d0 = (double)pos.getX() + 0.5D + 0.25D * (double)k;
               d3 = (double)(random.nextFloat() * 2.0F * (float)k);
            }

            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
         }
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
		if(state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			if(level.getChunk(pos).getExistingBlockEntity(pos) instanceof WaystoneBlockEntity waystone) {
				if(stack.hasTag()) {
					if(stack.getTag().contains("linked_pos")) {
						BlockPos target = NbtUtils.readBlockPos(stack.getTag().getCompound("linked_pos"));
						if(waystone.trySetTarget(target)) {
							if(level.getChunk(target).getExistingBlockEntity(target) instanceof WaystoneBlockEntity targetentity) {
								targetentity.trySetTarget(pos);
								waystone.validateLink();
								targetentity.validateLink();
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void playerWillDestroy(Level level, BlockPos blockpos, BlockState blockstate, Player player) {
		if(!level.isClientSide) {
			if(player.isCreative()) {
				if (blockstate.getValue(HALF) == DoubleBlockHalf.UPPER) {
					BlockPos newblockpos = blockpos.below();
					BlockState newblockstate = level.getBlockState(newblockpos);
					if (newblockstate.is(blockstate.getBlock()) && newblockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
						BlockState blockstate1 = newblockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
						level.setBlock(newblockpos, blockstate1, 35);
						level.levelEvent(player, 2001, newblockpos, Block.getId(newblockstate));
					}
				}		
			}
			
			if(blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
				if(level.getChunk(blockpos).getExistingBlockEntity(blockpos) instanceof WaystoneBlockEntity waystone) {
					if(waystone.getLinkedPos() != null) {
						if(level.getChunk(waystone.getLinkedPos()).getExistingBlockEntity(waystone.getLinkedPos()) instanceof WaystoneBlockEntity targetentity) {
							if(targetentity.validateLink()) {
								targetentity.clearTarget();
							}
						}
					}
				}
			}
		}
		super.playerWillDestroy(level, blockpos, blockstate, player);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos thispos, Block block, BlockPos otherpos, boolean bool) {
	      boolean flag = level.hasNeighborSignal(thispos) || level.hasNeighborSignal(thispos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
	      if (!this.defaultBlockState().is(block) && flag != state.getValue(POWERED)) {

	         level.setBlock(thispos, state.setValue(POWERED, Boolean.valueOf(flag)), 2);
	      }

	   }

}
