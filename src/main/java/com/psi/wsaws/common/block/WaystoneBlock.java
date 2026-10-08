package com.psi.wsaws.common.block;

import com.mojang.serialization.MapCodec;
import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.blockentity.BlockEntityInit;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockEntity;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockTopEntity;
import com.psi.wsaws.common.item.ItemInit;
import com.psi.wsaws.common.util.DataComponentTypeInit;
import com.psi.wsaws.common.util.WaystoneLinkedPos;
import com.psi.wsaws.common.util.WaystoneValidationResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WaystoneBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
	protected static final VoxelShape SHAPE = Shapes.or(
			Shapes.or(
					Block.box(0, 27, 0, 16, 32, 16),
					Block.box(1, 23, 1,15, 27, 15)
			),
			Block.box(3, 8, 3, 13, 24, 13),
			Shapes.or(
					Block.box(1, 5, 1,15, 9, 15),
					Block.box(0, 0, 0,16, 5, 16)
			)
	);
	

	public WaystoneBlock(BlockSetType type, Properties properties) {
		super(properties.sound(type.soundType()));
	    this.registerDefaultState(
	    		this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
	    		.setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(POWERED, Boolean.FALSE)
	    		.setValue(WATERLOGGED, Boolean.FALSE)
				.setValue(LEVEL, 5)
		);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> state) {
		state.add(FACING, HALF, POWERED, WATERLOGGED, LEVEL);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? SHAPE : SHAPE.move(0,-1,0);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_60550_) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean hasDynamicLightEmission(BlockState state) {
		return true;
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getValue(LEVEL);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ?
				BlockEntityInit.WAYSTONE_BLOCK_TOP_ENTITY.get().create(pos, state) :
				BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get().create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entitytype) {
		return createTickerHelper(entitytype, BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get(), WaystoneBlockEntity::tick);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		for(int j = 0; j < 4; ++j) {
			double x = (double)pos.getX() + random.nextDouble();
			double y = (double)pos.getY() + random.nextDouble();
			double z = (double)pos.getZ() + random.nextDouble();
			double speedX = (random.nextDouble() - 0.5D) * 0.5D;
			double speedY = (random.nextDouble() - 0.5D) * 0.5D;
			double speedZ = (random.nextDouble() - 0.5D) * 0.5D;
			int k = random.nextInt(2) * 2 - 1;
			if (random.nextBoolean()) {
				z = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
				speedZ = (double)(random.nextFloat() * 2.0F * (float)k);
			} else {
				x = (double)pos.getX() + 0.5D + 0.25D * (double)k;
				speedX = (double)(random.nextFloat() * 2.0F * (float)k);
			}

			level.addParticle(ParticleTypes.PORTAL, x, y, z, speedX, speedY, speedZ);
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
		//WSaWS.LOGGER.debug("updating state for {}", state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF));
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		if(level.getBlockEntity(pos) instanceof WaystoneBlockEntity waystone){
			int emission = Math.max(0, waystone.getBottomState().getLightEmission(level, pos));
			//WSaWS.LOGGER.info("{} has block {} with emission level of {}", waystone, waystone.getBottomState(), emission);
			state.setValue(LEVEL, emission);
		} else if (level.getBlockEntity(pos) instanceof WaystoneBlockTopEntity waystone) {
			int emission = Math.max(0, waystone.getTopState().getLightEmission(level, pos));
			//WSaWS.LOGGER.info("{} has block {} with emission level of {}", waystone, waystone.getTopState(), emission);
			state.setValue(LEVEL, emission);
		}
		if(direction.getAxis().isVertical()) {
			if(neighborState.getBlock() instanceof WaystoneBlock && neighborState.getValue(HALF) != doubleblockhalf){
				//WSaWS.LOGGER.debug("neighbor is waystone and other half");
				return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
			} else {
				return (doubleblockhalf == DoubleBlockHalf.LOWER) == (direction == Direction.DOWN)
						? super.updateShape(state, direction, neighborState, level, pos, neighborPos)
						: Blocks.AIR.defaultBlockState();
			}
		}

		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context)) {
			boolean flag = level.hasNeighborSignal(blockpos)
					|| level.hasNeighborSignal(
					level.getBlockState(blockpos).getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER
							? blockpos.above()
							: blockpos.below()
			);
			FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
			return this.defaultBlockState()
					.setValue(FACING, context.getHorizontalDirection().getOpposite())
					.setValue(POWERED, Boolean.valueOf(flag))
					.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8));
		} else {
			return null;
		}
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		level.setBlock(pos.above(), state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
		if(state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			if(level.getChunk(pos).getBlockEntity(pos) instanceof WaystoneBlockEntity waystone) {
				//replicate data components to block entity
				if(stack.has(DataComponentTypeInit.WAYSTONE_LINKED_POS)) {
					BlockPos target = BlockPos.of(stack.get(DataComponentTypeInit.WAYSTONE_LINKED_POS).pos());
					waystone.trySetTarget(target);
				}
			}
		}
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER || level.getBlockState(pos.below()).is(this);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if(!level.isClientSide() && (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos))){


		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
		List<ItemStack> drops = new ArrayList<>();
		if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
			if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof WaystoneBlockEntity waystone) {
				drops.addAll(Block.getDrops(waystone.getBottomState(), (ServerLevel) params.getLevel(), waystone.getBlockPos(), waystone));
				drops.addAll(Block.getDrops(waystone.getCoreState(), (ServerLevel) params.getLevel(), waystone.getBlockPos(), waystone));
			}
		} else {
			if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof WaystoneBlockTopEntity waystone) {
				drops.addAll(Block.getDrops(waystone.getTopState(), (ServerLevel) params.getLevel(), waystone.getBlockPos(), waystone));
			}
		}


		return drops;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos thispos, Block block, BlockPos otherpos, boolean bool) {
		if(!level.isClientSide()){
			boolean flag = level.hasNeighborSignal(thispos)
					|| level.hasNeighborSignal(thispos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
			if (!this.defaultBlockState().is(block) && flag != state.getValue(POWERED)) {
				level.setBlock(thispos, state.setValue(POWERED, flag), 2);
			}
		}
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
							waystone.requestTeleport((ServerLevel) level, (ServerPlayer) player);
							return InteractionResult.CONSUME;
						} else {
							return InteractionResult.FAIL;
						}
					}
				}
			}
		}
		return InteractionResult.SUCCESS;
	}
		

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
			if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				return useItemOn(stack, level.getBlockState(pos.below()), level, pos.below(), player, hand, hitResult);
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
							waystone.requestTeleport((ServerLevel) level, (ServerPlayer) player);
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
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return null;
	}
}
