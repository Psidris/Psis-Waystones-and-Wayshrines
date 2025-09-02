package com.psi.wsaws.datagen;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLoot extends BlockLootSubProvider {
	   private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get()).map(ItemLike::asItem).collect(Collectors.toSet());
	
	protected ModBlockLoot() {
		super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		this.dropSelf(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get());
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		// TODO Auto-generated method stub
		return BlockInit.BLOCKS.getEntries().stream().flatMap(RegistryObject::stream)::iterator;
	}
}
