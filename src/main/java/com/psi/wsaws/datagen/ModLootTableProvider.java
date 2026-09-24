package com.psi.wsaws.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModLootTableProvider extends LootTableProvider {

	public ModLootTableProvider(PackOutput pack, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(pack, Set.of(), List.of(new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)), lookupProvider);
		// TODO Auto-generated constructor stub
	}

}

class ModBlockLoot extends BlockLootSubProvider {
	private static final Set<Item> EXPLOSION_RESISTANT = Stream.of(BlockInit.WAYSTONE_BLOCK.get()).map(ItemLike::asItem).collect(Collectors.toSet());
	
	protected ModBlockLoot(HolderLookup.Provider lookupProvider) {
		super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), lookupProvider);
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return BlockInit.BLOCKS.getEntries().stream().map(e -> (Block) e.value()).toList();
	}

	@Override
	public void generate() {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        
        /*//waystones
		this.add(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get(), block -> {
			return this.createSinglePropConditionTable(block, DoorBlock.HALF, DoubleBlockHalf.LOWER);
		});
		*/
		
		this.add(BlockInit.WAYSTONE_BLOCK.get(), noDrop());
		
		this.dropSelf(BlockInit.RIFT_CORE_BLOCK.get());
		
		//resonance crystals
		this.dropSelf(BlockInit.RESONANCE_CRYSTAL_BLOCK.get());
		this.add(BlockInit.RESONANCE_CRYSTAL_CLUSTER.get(), (block) -> {
			return createSilkTouchDispatchTable(block,
					LootItem.lootTableItem(ItemInit.RESONANCE_SHARD.get())
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
					.apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
					.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
					.otherwise(this.applyExplosionDecay(block, LootItem.lootTableItem(ItemInit.RESONANCE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))));
		});
		this.dropWhenSilkTouch(BlockInit.SMALL_RESONANCE_CRYSTAL_BUD.get());
		this.dropWhenSilkTouch(BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD.get());
		this.dropWhenSilkTouch(BlockInit.LARGE_RESONANCE_CRYSTAL_BUD.get());
	    this.add(BlockInit.BUDDING_RESONANCE_CRYSTAL.get(), noDrop());
	}
}