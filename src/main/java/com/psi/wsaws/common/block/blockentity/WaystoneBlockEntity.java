package com.psi.wsaws.common.block.blockentity;

import java.util.UUID;

import javax.annotation.Nullable;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.util.ChunkHandler;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.event.sound.SoundEvent;

public class WaystoneBlockEntity extends BlockEntity {
	@Nullable
	private BlockPos linked_pos;

	public WaystoneBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get(), pos, state);
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if(tag.contains("linked_pos")) {
			this.linked_pos = NbtUtils.readBlockPos(tag.getCompound("linked_pos"));
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if(this.linked_pos != null) {
			nbt.put("linked_pos", NbtUtils.writeBlockPos(linked_pos));
		}
	}

	public BlockPos getLinkedPos() {
		return linked_pos;
	}
	
	public void clearTarget() {
		trySetTarget(null);
	}
	
	public boolean trySetTarget(@Nullable BlockPos pos) {
		if(linked_pos == null) {
			linked_pos = pos;
			setChanged();
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
			level.blockUpdated(worldPosition, state.getBlock());
			WSaWS.LOGGER.debug("Target of {} successfully set to {}", worldPosition, pos);
			return true;
		} else return validateLink();
	}
	
	public boolean validateLink() {
		if(linked_pos != null) {
			if(level.getChunk(linked_pos).getExistingBlockEntity(linked_pos) instanceof WaystoneBlockEntity targetentity) {
				if (targetentity.getLinkedPos() == worldPosition) {
					WSaWS.LOGGER.debug("Link Validated.");
					return true;
				} else {
					WSaWS.LOGGER.debug("Waystone at {} not linked to waystone at {}.", linked_pos, worldPosition);
				}
			} else {
				WSaWS.LOGGER.debug("Target at {} of waystone at {} is not a waystone.", linked_pos, this.getBlockPos());
			}
		}else {
			WSaWS.LOGGER.debug("Target of {} is {}", worldPosition, linked_pos);
		}
		
		return false;
	}
	
	public void requestTeleport(ServerPlayer player) {
		player.teleportToWithTicket(linked_pos.getX(), linked_pos.getY(), linked_pos.getZ());
		level.playLocalSound(this.getBlockPos(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 0.5F, RandomSource.create().nextFloat() * 0.4F + 0.8F, false);
		level.playLocalSound(linked_pos, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 0.5F, RandomSource.create().nextFloat() * 0.4F + 0.8F, false);
		WSaWS.LOGGER.debug("teleport success");
	}
	
	@Override
	public void setRemoved() {
		
		super.setRemoved();
	}

	public static void tick(Level level, BlockPos pos, BlockState state, WaystoneBlockEntity entity) {
		
	}
}
