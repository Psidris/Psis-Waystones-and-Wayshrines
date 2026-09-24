package com.psi.wsaws.common.block;

import com.psi.wsaws.WSaWS;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.SculkSpreader.ChargeCursor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingResonanceCrystalBlock extends ResonanceCrystalBlock implements SculkBehaviour {
	public static final int GROWTH_CHANCE = 5;
	private static final Direction[] DIRECTIONS = Direction.values();

	public BuddingResonanceCrystalBlock(Properties p_49795_) {
		super(p_49795_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		//WSaWS.LOGGER.debug("resonance crystal random tick");
		if (random.nextInt(5) == 0) {
			//WSaWS.LOGGER.debug("resonance crystal random tick trigger");
			growClusters(level, pos, random);
		}
	}

	@Override
	public int attemptUseCharge(ChargeCursor cursor, LevelAccessor level, BlockPos pos, RandomSource random, SculkSpreader spreader, boolean shouldConvertBlocks) {
		if(level.getBlockState(cursor.getPos()).is(this)) {
			if (cursor.getCharge() != 0) {
				//WSaWS.LOGGER.debug("bud finna grow");
				if (random.nextInt(spreader.growthSpawnCost()) < cursor.getCharge()) {
					//WSaWS.LOGGER.debug("bud growing");
					growClusters(level, cursor.getPos(), random);
					return Math.max(0, cursor.getCharge() - spreader.growthSpawnCost());
				}
			}
		}
		
		
		return cursor.getCharge();
	}

	public static boolean canClusterGrowAtState(BlockState state) {
		return state.isAir() || state.is(Blocks.SCULK_VEIN) || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
	}
	
	private void growClusters(LevelAccessor level, BlockPos pos, RandomSource random) {
		Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
		BlockPos blockpos = pos.relative(direction);
		BlockState blockstate = level.getBlockState(blockpos);
		Block block = null;
		if (canClusterGrowAtState(blockstate)) {
			block = BlockInit.SMALL_RESONANCE_CRYSTAL_BUD.get();
		} else if (blockstate.is(BlockInit.SMALL_RESONANCE_CRYSTAL_BUD.get()) && blockstate.getValue(ResonanceCrystalClusterBlock.FACING) == direction) {
			block = BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD.get();
		} else if (blockstate.is(BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD.get()) && blockstate.getValue(ResonanceCrystalClusterBlock.FACING) == direction) {
			block = BlockInit.LARGE_RESONANCE_CRYSTAL_BUD.get();
		} else if (blockstate.is(BlockInit.LARGE_RESONANCE_CRYSTAL_BUD.get()) && blockstate.getValue(ResonanceCrystalClusterBlock.FACING) == direction) {
			block = BlockInit.RESONANCE_CRYSTAL_CLUSTER.get();
		}

		if (block != null) {
			BlockState blockstate1 = block.getStateDefinition().any()
					.setValue(ResonanceCrystalClusterBlock.FACING, direction)
					.setValue(ResonanceCrystalClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
			level.setBlock(blockpos, blockstate1, 3);
		}
	}
}
