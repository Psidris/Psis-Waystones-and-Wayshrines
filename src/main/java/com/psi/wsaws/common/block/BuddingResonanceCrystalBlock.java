package com.psi.wsaws.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingResonanceCrystalBlock extends ResonanceCrystalBlock {
	public static final int GROWTH_CHANCE = 5;
	private static final Direction[] DIRECTIONS = Direction.values();

	public BuddingResonanceCrystalBlock(Properties p_49795_) {
		super(p_49795_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextInt(5) == 0) {
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
				BlockState blockstate1 = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)
						.setValue(AmethystClusterBlock.WATERLOGGED,
								Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
				level.setBlockAndUpdate(blockpos, blockstate1);
			}

		}
	}

	public static boolean canClusterGrowAtState(BlockState state) {
		return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
	}
}
