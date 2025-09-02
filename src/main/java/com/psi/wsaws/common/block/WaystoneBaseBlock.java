package com.psi.wsaws.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class WaystoneBaseBlock extends HorizontalDirectionalBlock {
	
	public static BlockSetType TYPE = BlockSetType.STONE;

	public WaystoneBaseBlock(Properties p_49795_, BlockSetType settype) {
		super(p_49795_);
		TYPE = settype;
	}

}
