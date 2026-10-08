package com.psi.wsaws.common.block;

import com.mojang.serialization.MapCodec;

import com.psi.wsaws.common.block.blockentity.BlockEntityInit;
import com.psi.wsaws.common.block.blockentity.WayshrineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WayshrineBlock extends BaseEntityBlock {

	protected WayshrineBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(
				blockEntityType,
				BlockEntityInit.WAYSHRINE_BLOCK_ENTITY.get(),
				level.isClientSide() ? WayshrineBlockEntity::clientTick : WayshrineBlockEntity::serverTick);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return null;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new WayshrineBlockEntity(pos, state);
	}
}
