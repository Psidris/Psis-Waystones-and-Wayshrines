package com.psi.wsaws.common.block.blockentity;

import javax.annotation.Nullable;

import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.util.ChunkHandler;
import com.psi.wsaws.common.util.WaystoneValidationResult;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class WaystoneBlockEntity extends BlockEntity {
	@Nullable
	private BlockPos linked_pos;
	private int radius = 3;
	public static final ModelProperty<TextureAtlasSprite> CORE_SPRITE = new ModelProperty<>();
	public static final ModelProperty<TextureAtlasSprite> BOTTOM_SPRITE = new ModelProperty<>();
	private BlockState bottomBlock = Blocks.COBBLED_DEEPSLATE.getStateDefinition().any();
	private BlockState coreBlock = BlockInit.RIFT_CORE_BLOCK.get().getStateDefinition().any();
	
	public WaystoneBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.WAYSTONE_BLOCK_ENTITY.get(), pos, state);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		if(tag.contains("linked_pos")) {
			this.linked_pos = BlockPos.of(tag.getLong("linked_pos"));
		}
		this.bottomBlock = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("bottom_state"));
		this.coreBlock = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("core_state"));

		requestModelDataUpdate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		if(linked_pos != null) {
			tag.putLong("linked_pos", linked_pos.asLong());
		}
		tag.put("bottom_state", NbtUtils.writeBlockState(bottomBlock));
		tag.put("core_state", NbtUtils.writeBlockState(coreBlock));
	}

	@Override
	public CompoundTag getUpdateTag(Provider registries) {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, registries);
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, Provider lookupProvider) {
		super.handleUpdateTag(tag, lookupProvider);
		if(level != null && level.isClientSide) {
			requestModelDataUpdate();
		}
	}

	public void setStates(BlockState bottom, BlockState core) {
		this.bottomBlock = bottom;
		this.coreBlock = core;
		setChanged();

		if(!level.isClientSide) {
			level.sendBlockUpdated(this.getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
			requestModelDataUpdate();
		}
	}
	
	public BlockState getTopState() {
		if(this.getLevel().getBlockEntity(this.getBlockPos().above()) instanceof WaystoneBlockTopEntity ent) {
			return ent.getTopState();
		} else return null;
	}
	public BlockState getBottomState() { return bottomBlock;}
	public BlockState getCoreState() { return coreBlock;}

	@Override
	public ModelData getModelData() {
		//WSaWS.LOGGER.debug("getModelData: bottom={}, core={}, top={}", bottomBlock, coreBlock, topBlock);
		return ModelData.builder()
				.with(CORE_SPRITE, getSpriteFor(coreBlock))
				.with(BOTTOM_SPRITE, getSpriteFor(bottomBlock))
				.build();
	}
	
	private TextureAtlasSprite getSpriteFor(BlockState state) {
		return Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon(ModelData.EMPTY);
	}

	public BlockPos getLinkedPos() {
		return linked_pos;
	}
	
	public void clearTarget() {
		linked_pos = null;
		setChanged();
		if(!level.isClientSide) {
			level.sendBlockUpdated(this.getBlockPos(), getBlockState(), getBlockState(), 2);
		}
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
					
					if(targetentity.getBlockState().getValue(BlockStateProperties.POWERED)) {
						//release chunk
						ChunkHandler.releaseChunkTicket((ServerLevel) level, linked_pos);
						return WaystoneValidationResult.LINK_INVALID;
					}
					
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
		if(!level.isClientSide && findFreePosition(linked_pos.relative(level.getBlockState(linked_pos).getValue(BlockStateProperties.HORIZONTAL_FACING)), radius) instanceof BlockPos pos && pos != null) {
			//WSaWS.LOGGER.debug("Target of {} is valid, teleporting", pos);
			player.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			level.playLocalSound(this.getBlockPos(), SoundEvents.PLAYER_TELEPORT, SoundSource.BLOCKS, 0.5F, RandomSource.create().nextFloat() * 0.4F + 0.8F, false);
			level.playLocalSound(linked_pos, SoundEvents.PLAYER_TELEPORT, SoundSource.BLOCKS, 0.5F, RandomSource.create().nextFloat() * 0.4F + 0.8F, false);
			//WSaWS.LOGGER.debug("teleport success");
		}else {
			//WSaWS.LOGGER.debug("teleport fail, position at {} not clear", linked_pos);
		}
	}
	
	private BlockPos findFreePosition(BlockPos pos, int radius) {
		//BlockPos.spiralAround(pos, radius, level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING), level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise())
		for(BlockPos check : BlockPos.withinManhattan(pos, radius, radius, radius)
				) {
			//WSaWS.LOGGER.debug("Checking if block at {} is air", check);
			if((level.getBlockState(check).isAir() || level.getBlockState(check).is(Blocks.WATER)) && (level.getBlockState(check.above()).isAir() || level.getBlockState(check.above()).is(Blocks.WATER))) {
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
