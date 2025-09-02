package com.psi.wsaws.datagen;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

public class ModLootTableProvider extends LootTableProvider {

	public ModLootTableProvider(PackOutput pack) {
		super(pack, Set.of(), List.of(
				new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)));
		// TODO Auto-generated constructor stub
	}

}

class ModBlockLoot extends BlockLootSubProvider {
	   private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get()).map(ItemLike::asItem).collect(Collectors.toSet());
	
	protected ModBlockLoot() {
		super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		// TODO Auto-generated method stub
		return BlockInit.BLOCKS.getEntries().stream().flatMap(RegistryObject::stream)::iterator;
	}

	@Override
	protected void generate() {
		this.dropSelf(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get());
	}
}