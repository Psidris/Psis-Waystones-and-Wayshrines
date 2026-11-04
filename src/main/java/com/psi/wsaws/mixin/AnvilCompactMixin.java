package com.psi.wsaws.mixin;

import com.psi.wsaws.common.block.WaystoneBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.data.internal.NeoForgeBlockTagsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockEntity;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockTopEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

@Mixin(AnvilBlock.class)
public class AnvilCompactMixin {
	
	@Inject(method = "onLand", at = @At("HEAD"), cancellable = true)
	private void wsaws$onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock, CallbackInfo ci) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;
        //WSaWS.LOGGER.debug("anvil crush event");
        
        if (level.getBlockState(pos.below(2)).is(BlockInit.RIFT_CORE_BLOCK.get())) {
            //WSaWS.LOGGER.debug("{} spotted", coreState);
			BlockPos corePos = pos.below(2);
			BlockState coreState = level.getBlockState(corePos);
            BlockState bottomState = level.getBlockState(corePos.below());
            BlockState aboveState = level.getBlockState(corePos.above());
            
			if (isBlockValid(bottomState, serverLevel, corePos.below()) && isBlockValid(aboveState, serverLevel, corePos.above())) {
				level.destroyBlock(pos, true);
				ci.cancel(); // skip vanilla's own onBrokenAfterFall (sound/particle handling)

				level.destroyBlock(corePos, false);
				level.destroyBlock(corePos.above(), false);
				level.destroyBlock(corePos.below(), false);
				BlockState blockstate = BlockInit.WAYSTONE_BLOCK.get().defaultBlockState()
						.setValue(BlockStateProperties.HORIZONTAL_FACING, coreState.getValue(BlockStateProperties.HORIZONTAL_FACING))
						.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
						.setValue(BlockStateProperties.POWERED, level.hasNeighborSignal(corePos.below()))
						.setValue(WaystoneBlock.LEVEL, Math.max(5, bottomState.getLightEmission(level, corePos.above())));

				level.setBlock(corePos.below(), blockstate, 3);
				
				blockstate = blockstate
						.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)
						.setValue(BlockStateProperties.POWERED, level.hasNeighborSignal(corePos))
						.setValue(WaystoneBlock.LEVEL, Math.max(5, aboveState.getLightEmission(level, corePos.above())));
				
				level.setBlock(corePos, blockstate, 3);

				if (level.getBlockEntity(corePos) instanceof WaystoneBlockTopEntity topEnt) {
					topEnt.setStates(aboveState);
				}
				if (level.getBlockEntity(corePos.below()) instanceof WaystoneBlockEntity ent) {
					ent.setStates(bottomState, coreState);
				}
			}
		}

	}

	private boolean isBlockValid(BlockState state, Level level, BlockPos pos){

		return !state.hasBlockEntity()
				&& state.isCollisionShapeFullBlock(level, pos)
				&& !(state.is(BlockTags.IMPERMEABLE) || state.is(BlockTags.LEAVES))
				;
	}
}
