package com.psi.wsaws.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.SculkSpreader.ChargeCursor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ResonanceCrystalBlock extends Block implements SculkBehaviour {

	public ResonanceCrystalBlock(Properties p_49795_) {
		super(p_49795_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onProjectileHit(Level p_152001_, BlockState p_152002_, BlockHitResult p_152003_, Projectile p_152004_) {
		if (!p_152001_.isClientSide) {
			BlockPos blockpos = p_152003_.getBlockPos();
			p_152001_.playSound((Player) null, blockpos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.0F, 0.5F + p_152001_.random.nextFloat() * 1.2F);
			p_152001_.playSound((Player) null, blockpos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 0.5F + p_152001_.random.nextFloat() * 1.2F);
		}

	}

	@Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos pos, RandomSource random, SculkSpreader spreader, boolean shouldConvertBlocks) {
		return cursor.getCharge();
    }
}
