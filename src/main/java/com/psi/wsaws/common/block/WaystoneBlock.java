package com.psi.wsaws.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;
import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.blockentity.BlockEntityInit;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockEntity;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockTopEntity;
import com.psi.wsaws.common.item.ItemInit;
import com.psi.wsaws.common.util.ChunkHandler;
import com.psi.wsaws.common.util.DataComponentTypeInit;
import com.psi.wsaws.common.util.WaystoneLinkedPos;
import com.psi.wsaws.common.util.WaystoneValidationResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
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
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
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
	protected static final VoxelShape SHAPE_LOWER = Shapes.join(Block.box(0, 0, 0, 16, 2, 16), Block.box(1, 0, 1, 15, 31, 15), BooleanOp.OR);
	protected static final VoxelShape SHAPE_UPPER = Shapes.join(Block.box(0, -32, 0, 16, -30, 16), Block.box(1, -16, 1, 15, 16, 15), BooleanOp.OR);
    private final BlockSetType type;
	

	public WaystoneBlock(BlockSetType type, Properties properties) {
		super(properties.sound(type.soundType()));
		this.type = type;
	    this.registerDefaultState(
	    		this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, Boolean.valueOf(false))
	    		.setValue(HALF, DoubleBlockHalf.LOWER));
	}
	
	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if(!level.isClientSide()) {
			if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				useWithoutItem(level.getBlockState(pos.below()), level, pos.below(), player, hitResult);
				return InteractionResult.PASS;
			} else {
				if(level.getBlockEntity(pos) != null) {
					//get reference to this waystone
					if(level.getBlockEntity(pos) instanceof WaystoneBlockEntity waystone) {
						if(waystone.validateLink() == WaystoneValidationResult.LINK_VALID) {
							waystone.requestTeleport((ServerPlayer) player);
							return InteractionResult.SUCCESS;
						} else {
							return InteractionResult.FAIL;
						}
					}
				}
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
		

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
			if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				useItemOn(stack, level.getBlockState(pos.below()), level, pos.below(), player, hand, hitResult);
				return ItemInteractionResult.FAIL;
			} else {
				//check if block entity is a waystone
				if(level.getBlockEntity(pos) instanceof WaystoneBlockEntity waystone) {
					//check if the item is a rift claw
					ItemStack item = player.getItemInHand(hand);
					if(item.is(ItemInit.RIFT_CLAW.get())) {
						//check if the waystone target is allowed to change
						if(waystone.validateLink() != WaystoneValidationResult.LINK_VALID) {
							if(item.has(DataComponentTypeInit.WAYSTONE_LINKED_POS)) {
								//load stored position to waystone
								waystone.trySetTarget(BlockPos.of(item.get(DataComponentTypeInit.WAYSTONE_LINKED_POS).pos()));
								item.remove(DataComponentTypeInit.WAYSTONE_LINKED_POS);
								//WSaWS.LOGGER.debug("Position " + pos.asLong() + " removed from the claw");
							} else {
								//clear the waystones target
								waystone.clearTarget();
								//put position on the Rift Claw
								item.set(DataComponentTypeInit.WAYSTONE_LINKED_POS, new WaystoneLinkedPos(pos.asLong()));
								//WSaWS.LOGGER.debug("Position " + pos.asLong() + " stored to the claw");
							}
						}
						return ItemInteractionResult.CONSUME;
					} else {
						if(waystone.validateLink() == WaystoneValidationResult.LINK_VALID) {
							waystone.requestTeleport((ServerPlayer) player);
							return ItemInteractionResult.CONSUME;
						} else {
							return ItemInteractionResult.FAIL;
						}
					}
				}
			}
		}
		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entitytype) {
		return createTickerHelper(entitytype, BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get(), WaystoneBlockEntity::tick);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if(level.getBlockEntity(pos) instanceof WaystoneBlockEntity ent && ent.getLinkedPos() != null) {
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
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? SHAPE_LOWER : SHAPE_UPPER;
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
			return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(POWERED, Boolean.valueOf(flag));
		} else {
			return null;
		}
	}
	

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
		if(state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			if(level.getChunk(pos).getBlockEntity(pos) instanceof WaystoneBlockEntity waystone) {
				//replicate data components to block entity
				if(stack.has(DataComponentTypeInit.WAYSTONE_LINKED_POS)) {
					BlockPos target = BlockPos.of(stack.get(DataComponentTypeInit.WAYSTONE_LINKED_POS).pos());
					waystone.trySetTarget(target);
				}
				if(stack.has(DataComponents.BLOCK_STATE)) {
					
				}
			}
		}
	}
	
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		/*
		if(player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos)) {
			if(!level.isClientSide) {
				DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
		        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
		            BlockPos blockpos = pos.below();
		            BlockState blockstate = level.getBlockState(blockpos);
		            if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
		                BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
		                level.setBlock(blockpos, blockstate1, 35);
		                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
		            }
		        }
			} else {
				dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}
		*/
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
		
		List<ItemStack> drops = new ArrayList<>();
		if(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
			if(params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof WaystoneBlockEntity waystone) {
				drops.addAll(Block.getDrops(waystone.getBottomState(), (ServerLevel) params.getLevel(), waystone.getBlockPos(), waystone));
				drops.addAll(Block.getDrops(waystone.getCoreState(), (ServerLevel) params.getLevel(), waystone.getBlockPos(), waystone));
			}
		} else if(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
			if(params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof WaystoneBlockTopEntity waystone) {
				drops.addAll(Block.getDrops(waystone.getTopState(), (ServerLevel) params.getLevel(), waystone.getBlockPos(), waystone));
			}
		}
		return drops;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos thispos, Block block, BlockPos otherpos, boolean bool) {
		boolean flag = level.hasNeighborSignal(thispos) || level.hasNeighborSignal(thispos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if (!this.defaultBlockState().is(block) && flag != state.getValue(POWERED)) {

			level.setBlock(thispos, state.setValue(POWERED, Boolean.valueOf(flag)), 2);
		}

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ? 
				BlockEntityInit.WAYSTONE_BLOCK_TOP_ENTITY.get().create(pos, state) :
				BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get().create(pos, state);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		// TODO Auto-generated method stub
		return null;
	}

}
