package com.psi.wsaws.datagen;

import java.util.function.Consumer;

import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(PackOutput p_248933_) {
		super(p_248933_);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		//items
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.CRYSTAL_SHELL.get())
			.define('A', Items.AMETHYST_CLUSTER)
			.define('Q', Items.QUARTZ)
			.pattern("AQA")
			.pattern("Q Q")
			.pattern("AQA")
			.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD, Items.QUARTZ))
			.save(consumer);
		
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(ItemInit.CRYSTAL_SHELL.get()), Ingredient.of(Items.ENDER_EYE), Ingredient.of(Items.NETHER_STAR), RecipeCategory.TOOLS, ItemInit.EVERLASTING_ENDER_PEARL.get())
			.unlocks("has_item", has(Items.NETHER_STAR))
			.save(consumer, "everlasting_ender_pearl_smithing");
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.WAYSTONE_CORE.get(), 2)
			.define('O', Items.CRYING_OBSIDIAN)
			.define('P', ItemInit.EVERLASTING_ENDER_PEARL.get())
			.define('C', Items.CHORUS_FRUIT)
			.define('E', Items.ECHO_SHARD)
			.pattern("OEO")
			.pattern("CPC")
			.pattern("OEO")
			.unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHORUS_FRUIT, Items.ECHO_SHARD))
			.save(consumer);
		
		//blocks
		ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get())
			.define('C', ItemInit.WAYSTONE_CORE.get())
			.define('D', Items.DEEPSLATE_BRICKS)
			.pattern("DDD")
			.pattern(" C ")
			.pattern("DDD")
			.unlockedBy("has_item", has(ItemInit.WAYSTONE_CORE.get()))
			.save(consumer);
	}

}
