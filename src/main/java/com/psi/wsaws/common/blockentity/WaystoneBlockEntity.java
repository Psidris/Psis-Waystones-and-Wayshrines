package com.psi.wsaws.common.blockentity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WaystoneBlockEntity extends BlockEntity {

	public WaystoneBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get(), pos, state);
		// TODO Auto-generated constructor stub
	}
}
