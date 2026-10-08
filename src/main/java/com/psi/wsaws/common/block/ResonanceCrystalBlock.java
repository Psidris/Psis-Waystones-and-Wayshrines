package com.psi.wsaws.common.block;

import com.psi.wsaws.common.item.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.SculkSpreader.ChargeCursor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ResonanceCrystalBlock extends Block implements SculkBehaviour {
	public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, 5);

	public ResonanceCrystalBlock(Properties p_49795_) {
		super(p_49795_);
		this.registerDefaultState(
				this.stateDefinition.any().setValue(PHASE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(PHASE);
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
		if (!level.isClientSide) {
			BlockPos blockpos = hitResult.getBlockPos();
			level.playSeededSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.0F, 0.5F + state.getValue(PHASE) * 0.1F, this.getSeed(state, blockpos));
			level.playSeededSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.5F + state.getValue(PHASE) * 0.1F, this.getSeed(state, blockpos));
		}

	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
			BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
			ItemStack item = player.getItemInHand(hand);
			if(item.is(ItemInit.RIFT_CLAW.get())) {
				BlockState newState = state.cycle(PHASE);
				level.setBlock(pos, newState, 2);
				level.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1F, 0.5F + newState.getValue(PHASE) * 0.1F, this.getSeed(state, pos));
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {

		return this.defaultBlockState()
				.setValue(PHASE, determinePhase(context.getLevel(), context.getClickedPos()))
				.cycle(PHASE);
	}

	public static int determinePhase(LevelAccessor level, BlockPos pos) {
		int lowest_phase = 0;
		for (Direction direction : Direction.values()) {
			if (level.getBlockState(pos.relative(direction)).hasProperty(PHASE)) {
				lowest_phase = lowest_phase > level.getBlockState(pos.relative(direction)).getValue(PHASE)
						? lowest_phase
						: level.getBlockState(pos.relative(direction)).getValue(PHASE);
			}
		}
		return lowest_phase;
	}

	@Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos pos, RandomSource random, SculkSpreader spreader, boolean shouldConvertBlocks) {
		return cursor.getCharge();
    }
}
