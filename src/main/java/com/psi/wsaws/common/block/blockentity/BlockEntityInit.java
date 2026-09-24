package com.psi.wsaws.common.block.blockentity;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityInit {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WSaWS.MODID);
	
	public static final Supplier<BlockEntityType<WaystoneBlockEntity>> WAYSTONE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
			"waystone_block_entity",
			() -> BlockEntityType.Builder.of(
					WaystoneBlockEntity::new,
					BlockInit.WAYSTONE_BLOCK.get()
	        ).build(null)
	);
	public static final Supplier<BlockEntityType<WaystoneBlockTopEntity>> WAYSTONE_BLOCK_TOP_ENTITY = BLOCK_ENTITY_TYPES.register(
			"waystone_block_top_entity",
			() -> BlockEntityType.Builder.of(
					WaystoneBlockTopEntity::new,
					BlockInit.WAYSTONE_BLOCK.get()
	        ).build(null)
	);
}
