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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WSaWS.MODID);
	
	//waystone blocks
	public static final RegistryObject<Block> WAYSTONE_BLOCK_DEEPSLATE = register("waystone_block_deepslate",
			() -> new WaystoneBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).requiresCorrectToolForDrops().lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	
	//Resonance Crystal Blocks
	public static final RegistryObject<Block> RESONANCE_CRYSTAL_BLOCK = register("resonance_crystal_block",
			() -> new ResonanceCrystalBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_BLACK).lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	public static final RegistryObject<Block> BUDDING_RESONANCE_CRYSTAL = register("budding_resonance_crystal",
			() -> new BuddingResonanceCrystalBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_BLACK).lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	public static final RegistryObject<Block> RESONANCE_CRYSTAL_CLUSTER = register("resonance_crystal_cluster",
			() -> new ResonanceCrystalClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).mapColor(MapColor.COLOR_BLACK).lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	public static final RegistryObject<Block> LARGE_RESONANCE_CRYSTAL_BUD = register("large_resonance_crystal_bud",
			() -> new ResonanceCrystalClusterBlock(5, 3, BlockBehaviour.Properties.copy(RESONANCE_CRYSTAL_CLUSTER.get())));
	public static final RegistryObject<Block> MEDIUM_RESONANCE_CRYSTAL_BUD = register("medium_resonance_crystal_bud",
			() -> new ResonanceCrystalClusterBlock(4, 3, BlockBehaviour.Properties.copy(RESONANCE_CRYSTAL_CLUSTER.get())));
	public static final RegistryObject<Block> SMALL_RESONANCE_CRYSTAL_BUD = register("small_resonance_crystal_bud",
			() -> new ResonanceCrystalClusterBlock(3, 4, BlockBehaviour.Properties.copy(RESONANCE_CRYSTAL_CLUSTER.get())));

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
