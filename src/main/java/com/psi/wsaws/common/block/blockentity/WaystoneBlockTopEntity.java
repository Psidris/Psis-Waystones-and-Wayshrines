package com.psi.wsaws.common.block.blockentity;

import com.psi.wsaws.WSaWS;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class WaystoneBlockTopEntity extends BlockEntity {
	public static final ModelProperty<TextureAtlasSprite> TOP_SPRITE = new ModelProperty<>();
	private BlockState topBlock = Blocks.COBBLED_DEEPSLATE.getStateDefinition().any();

	public WaystoneBlockTopEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.WAYSTONE_BLOCK_TOP_ENTITY.get(), pos, state);
	}
	
	@Override
	protected void loadAdditional(CompoundTag tag, Provider registries) {
		super.loadAdditional(tag, registries);
		this.topBlock = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("top_state"));

		requestModelDataUpdate();
		
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("top_state", NbtUtils.writeBlockState(topBlock));
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

	public void setStates(BlockState top) {
		this.topBlock = top;
		setChanged();

		if(!level.isClientSide) {
			level.sendBlockUpdated(this.getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
			requestModelDataUpdate();
		}
	}
	
	public BlockState getTopState() { return topBlock;}

	@Override
	public ModelData getModelData() {
		return ModelData.builder()
				.with(TOP_SPRITE, getSpriteFor(topBlock))
				.build();
	}
	
	private TextureAtlasSprite getSpriteFor(BlockState state) {
		return Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon(ModelData.EMPTY);
	}

}
