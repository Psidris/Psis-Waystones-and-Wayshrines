package com.psi.wsaws.datagen;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
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
		//waystones
		this.add(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get(), block -> {
			return this.createDoorTable(block);
		});
		
		//resonance crystals
		this.dropSelf(BlockInit.RESONANCE_CRYSTAL_BLOCK.get());
		this.add(BlockInit.RESONANCE_CRYSTAL_CLUSTER.get(), (block) -> {
			return createSilkTouchDispatchTable(block,
					LootItem.lootTableItem(ItemInit.RESONANCE_SHARD.get())
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
					.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
					.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
					.otherwise(this.applyExplosionDecay(block, LootItem.lootTableItem(ItemInit.RESONANCE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))));
		});
		this.dropWhenSilkTouch(BlockInit.SMALL_RESONANCE_CRYSTAL_BUD.get());
		this.dropWhenSilkTouch(BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD.get());
		this.dropWhenSilkTouch(BlockInit.LARGE_RESONANCE_CRYSTAL_BUD.get());
	    this.add(BlockInit.BUDDING_RESONANCE_CRYSTAL.get(), noDrop());
	}
}