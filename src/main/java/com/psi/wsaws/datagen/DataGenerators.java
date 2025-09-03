package com.psi.wsaws.datagen;

import java.util.List;

import com.psi.wsaws.WSaWS;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WSaWS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	private DataGenerators() {
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		
		//generate block states, block models, and item models
		gen.addProvider(true, new ModBlockStateProvider(gen.getPackOutput(), existingFileHelper));
		gen.addProvider(true, new ModItemModelProvider(gen.getPackOutput(), existingFileHelper));
		
		//generate recipes
		gen.addProvider(true, new ModRecipeProvider(gen.getPackOutput()));
		//generate block tags
		gen.addProvider(true, new ModBlockTagProvider(gen.getPackOutput(), event.getLookupProvider(), existingFileHelper));
		//generate loot tables
		gen.addProvider(true, new ModLootTableProvider(gen.getPackOutput()));
		
		//generate advancements
		gen.addProvider(true, new ModAdvancementsProvider(gen.getPackOutput(), event.getLookupProvider(), List.of(new ModAdvancementsSubProvider())));
	}
}
