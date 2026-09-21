package com.psi.wsaws.common.block;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.item.ItemInit;
import com.psi.wsaws.common.util.CreativeTabInit;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, WSaWS.MODID);
	
	//waystone blocks
	public static final DeferredHolder<Block, WaystoneBlock> WAYSTONE_BLOCK_DEEPSLATE = register("waystone_block_deepslate",
			() -> new WaystoneBlock(BlockSetType.STONE, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).requiresCorrectToolForDrops().lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	
	//Resonance Crystal Blocks
	public static final DeferredHolder<Block, ResonanceCrystalBlock> RESONANCE_CRYSTAL_BLOCK = register("resonance_crystal_block",
			() -> new ResonanceCrystalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_BLACK).lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	public static final DeferredHolder<Block, BuddingResonanceCrystalBlock> BUDDING_RESONANCE_CRYSTAL = register("budding_resonance_crystal",
			() -> new BuddingResonanceCrystalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_BLACK).lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	public static final DeferredHolder<Block, ResonanceCrystalClusterBlock> RESONANCE_CRYSTAL_CLUSTER = register("resonance_crystal_cluster",
			() -> new ResonanceCrystalClusterBlock(7, 3, BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER).mapColor(MapColor.COLOR_BLACK).lightLevel((p_50828_) -> {
			      return 5;
			   }).noOcclusion()));
	public static final DeferredHolder<Block, ResonanceCrystalClusterBlock> LARGE_RESONANCE_CRYSTAL_BUD = register("large_resonance_crystal_bud",
			() -> new ResonanceCrystalClusterBlock(5, 3, BlockBehaviour.Properties.ofFullCopy(RESONANCE_CRYSTAL_CLUSTER.get())));
	public static final DeferredHolder<Block, ResonanceCrystalClusterBlock> MEDIUM_RESONANCE_CRYSTAL_BUD = register("medium_resonance_crystal_bud",
			() -> new ResonanceCrystalClusterBlock(4, 3, BlockBehaviour.Properties.ofFullCopy(RESONANCE_CRYSTAL_CLUSTER.get())));
	public static final DeferredHolder<Block, ResonanceCrystalClusterBlock> SMALL_RESONANCE_CRYSTAL_BUD = register("small_resonance_crystal_bud",
			() -> new ResonanceCrystalClusterBlock(3, 4, BlockBehaviour.Properties.ofFullCopy(RESONANCE_CRYSTAL_CLUSTER.get())));

	public static void register() {
	};

	private static <T extends Block> DeferredHolder<Block, T> registerNoItem(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	private static <T extends Block> DeferredHolder<Block, T> register(String name, Supplier<T> block) {
		DeferredHolder<Block, T> ret = registerNoItem(name, block);
		CreativeTabInit.addToTab(ItemInit.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties())));
		return ret;
	}
}
