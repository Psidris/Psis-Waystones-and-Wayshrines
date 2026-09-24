package com.psi.wsaws.mixin;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;

@Mixin(BuddingAmethystBlock.class)
@Implements(@Interface(iface = SculkBehaviour.class, prefix = "sculk$"))
public class BuddingAmethystSculkBehaviourMixin {
	public int sculk$attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos pos, RandomSource random, SculkSpreader spreader, boolean shouldConvertBlocks) {
		//WSaWS.LOGGER.debug("we're in");
		if(level.getBlockState(cursor.getPos()).is(Blocks.BUDDING_AMETHYST)) {
			if (level.setBlock(cursor.getPos(), BlockInit.BUDDING_RESONANCE_CRYSTAL.get().getStateDefinition().any(), 3)) {
				//WSaWS.LOGGER.debug("crystal replaced"); 
				return cursor.getCharge()-1;
			} else {
				//WSaWS.LOGGER.debug("crystal not replaced");
			}
			
		}
		//WSaWS.LOGGER.debug("didnt work");
		return cursor.getCharge();
		
	}
}
