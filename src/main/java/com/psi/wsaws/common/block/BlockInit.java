package com.psi.wsaws.common.block;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.CreativeTabInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WSaWS.MODID);

	public static final RegistryObject<WaystoneBlock> WAYSTONE_BLOCK_DEEPSLATE = register("waystone_block_deepslate",
			() -> new WaystoneBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).noCollission()));

	public static void register() {
	};

	private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		RegistryObject<T> ret = registerNoItem(name, block);
		CreativeTabInit.addToTab(ItemInit.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties())));
		return ret;
	}
}
