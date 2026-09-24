package com.psi.wsaws.datagen;

import java.util.concurrent.CompletableFuture;

import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class ModRecipeProvider extends RecipeProvider {
	
	public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		//items
				ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemInit.RIFT_CLAW.get())
					.define('R', ItemInit.RESONANCE_SHARD.get())
					.define('L', Items.LEATHER)
					.define('B', Items.LEAD)
					.pattern("R R")
					.pattern("RLR")
					.pattern("LBL")
					.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.RESONANCE_SHARD.get()))
					.save(recipeOutput);
				
				ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.CRYSTAL_SHELL.get())
					.define('R', BlockInit.RESONANCE_CRYSTAL_CLUSTER.get())
					.define('E', Items.ECHO_SHARD)
					.pattern("RER")
					.pattern("E E")
					.pattern("RER")
					.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD, Items.QUARTZ))
					.save(recipeOutput);
				
				SmithingTransformRecipeBuilder.smithing(Ingredient.of(ItemInit.CRYSTAL_SHELL.get()), Ingredient.of(Items.ENDER_EYE), Ingredient.of(Items.NETHER_STAR), RecipeCategory.TOOLS, ItemInit.EVERLASTING_ENDER_PEARL.get())
					.unlocks("has_item", has(Items.NETHER_STAR))
					.save(recipeOutput, "everlasting_ender_pearl_smithing");
				
				//blocks
				ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.RESONANCE_CRYSTAL_BLOCK.get(), 2)
					.define('R', ItemInit.RESONANCE_SHARD.get())
					.pattern("RR")
					.pattern("RR")
					.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.RESONANCE_SHARD.get()))
					.save(recipeOutput);
				
				ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.RIFT_CORE_BLOCK.get(), 2)
					.define('O', Items.CRYING_OBSIDIAN)
					.define('P', ItemInit.EVERLASTING_ENDER_PEARL.get())
					.define('C', Items.CHORUS_FRUIT)
					.define('R', ItemInit.RESONANCE_SHARD.get())
					.pattern("OCO")
					.pattern("RPR")
					.pattern("OCO")
					.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHORUS_FRUIT, ItemInit.RESONANCE_SHARD.get()))
					.save(recipeOutput);
				
				ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, BlockInit.WAYSTONE_BLOCK.get())
					.define('C', BlockInit.RIFT_CORE_BLOCK.get())
					.define('D', Items.COBBLED_DEEPSLATE)
					.pattern("D")
					.pattern("C")
					.pattern("D")
					.unlockedBy("has_item", has(BlockInit.RIFT_CORE_BLOCK.get()))
					.save(recipeOutput);
	}
}
