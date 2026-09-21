package com.psi.wsaws.common.block.blockentity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.WaystoneBlock;
import com.psi.wsaws.common.util.ChunkHandler;
import com.psi.wsaws.common.util.DataComponentTypeInit;
import com.psi.wsaws.common.util.WaystoneLinkedPos;
import com.psi.wsaws.common.util.WaystoneValidationResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaystoneBlockEntity extends BlockEntity {
	@Nullable
	private BlockPos linked_pos;
	private int radius = 3;
	
	public WaystoneBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		if(tag.contains("linked_pos")) {
			this.linked_pos = BlockPos.of(tag.getLong("linked_pos"));
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		if(linked_pos != null) {
			tag.putLong("linked_pos", linked_pos.asLong());
		}
	}

	@Override
	public void saveToItem(ItemStack stack, Provider registries) {
		super.saveToItem(stack, registries);
		if(linked_pos != null) {
			stack.set(DataComponentTypeInit.WAYSTONE_LINKED_POS, new WaystoneLinkedPos(linked_pos.asLong()));
		}
	}

	public BlockPos getLinkedPos() {
		return linked_pos;
	}
	
	public void clearTarget() {
		linked_pos = null;
		setChanged();
		level.sendBlockUpdated(this.getBlockPos(), getBlockState(), getBlockState(), 2);
		//WSaWS.LOGGER.debug("Target of {} successfully set to {}", this.getBlockPos(), linked_pos);
	}
	
	public WaystoneValidationResult trySetTarget(@Nullable BlockPos pos) {
		if(linked_pos == null && !level.isClientSide) {
			if(pos.compareTo(this.getBlockPos()) == 0) {
				clearTarget();
				return WaystoneValidationResult.LINK_INVALID;
			}
			linked_pos = pos;
			setChanged();
			level.sendBlockUpdated(pos, getBlockState(), getBlockState(), 2);
			//WSaWS.LOGGER.debug("Target of {} successfully set to {}", this.getBlockPos(), linked_pos);
			return validateLink();
		} else return validateLink();
	}
	
	public WaystoneValidationResult validateLink() {
		if(linked_pos != null && !level.isClientSide) {
			if(level.getChunk(linked_pos).getBlockEntity(linked_pos) instanceof WaystoneBlockEntity targetentity) {
				if(targetentity == this) {
					//WSaWS.LOGGER.debug("Waystone at {} linked to itself, removing link", this.getBlockPos());
					targetentity.clearTarget();
					return WaystoneValidationResult.LINK_INVALID;
				}
				if(level.hasChunkAt(linked_pos)) {
					//load chunk at linked pos
					ChunkHandler.registerChunkTicket((ServerLevel) level, linked_pos);
				}
				//check if target waystone link matches current waystone
				if (targetentity.getLinkedPos() != null && targetentity.getLinkedPos().compareTo(this.getBlockPos()) == 0) {
					//WSaWS.LOGGER.debug("Link Validated.");
					//release chunk
					ChunkHandler.releaseChunkTicket((ServerLevel) level, linked_pos);
					return WaystoneValidationResult.LINK_VALID;
				} else {
					//check if target waystone link is vacant
					if(targetentity.getLinkedPos() == null) {
						//WSaWS.LOGGER.debug("Waystone at {} not linked, assigning to waystone at {}", linked_pos, this.getBlockPos());
						targetentity.trySetTarget(this.getBlockPos());
						//release chunk
						ChunkHandler.releaseChunkTicket((ServerLevel) level, linked_pos);
						return WaystoneValidationResult.LINK_VACANT;
					} else {
						//WSaWS.LOGGER.debug("Waystone at {} not linked to waystone at {}, instead it is linked to {}.", linked_pos, this.getBlockPos(), targetentity.getLinkedPos());
						//release chunk
						ChunkHandler.releaseChunkTicket((ServerLevel) level, linked_pos);
						return WaystoneValidationResult.LINK_INVALID;
					}
				}
			} else {
				//WSaWS.LOGGER.debug("Target at {} of waystone at {} is not a waystone.", linked_pos, this.getBlockPos());
				return WaystoneValidationResult.LINK_BROKEN;
			}
		}else {
			//WSaWS.LOGGER.debug("Target of {} is {}", this.getBlockPos(), linked_pos);
			return WaystoneValidationResult.POS_EMPTY;
		}
	}
	
	public void requestTeleport(ServerPlayer player) {
		if(findFreePosition(linked_pos, radius) instanceof BlockPos pos && pos != null) {
			//WSaWS.LOGGER.debug("Target of {} is valid, teleporting", pos);
			player.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			level.playLocalSound(this.getBlockPos(), SoundEvents.PLAYER_TELEPORT, SoundSource.BLOCKS, 0.5F, RandomSource.create().nextFloat() * 0.4F + 0.8F, false);
			level.playLocalSound(linked_pos, SoundEvents.PLAYER_TELEPORT, SoundSource.BLOCKS, 0.5F, RandomSource.create().nextFloat() * 0.4F + 0.8F, false);
			//WSaWS.LOGGER.debug("teleport success");
		}else {
			//WSaWS.LOGGER.debug("teleport fail, position at {} not clear", linked_pos);
		}
	}
	
	public BlockPos findFreePosition(BlockPos pos, int radius) {
		for(BlockPos check : BlockPos.MutableBlockPos.spiralAround(
				pos, 
				radius, 
				level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING), 
				level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise())
				) {
			//WSaWS.LOGGER.debug("Checking if block at {} is air", check);
			if(level.getBlockState(check).isAir() && level.getBlockState(check.above()).isAir()) {
				return check;
			}
		}
		
		return null;
	}
	
	@Override
	public void setRemoved() {
		
		super.setRemoved();
	}

	public static void tick(Level level, BlockPos pos, BlockState state, WaystoneBlockEntity entity) {
		
	}
}
