package com.psi.wsaws.common.blockentity;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WSaWS.MODID);
	
	public static final RegistryObject<BlockEntityType<WaystoneBlockEntity>> WAYSTONE_BLOCK_ENTITY = register("waystone_block_entity", WaystoneBlockEntity::new, BlockInit.WAYSTONE_BLOCK_DEEPSLATE);
	
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, RegistryObject<? extends Block> block){
		return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
