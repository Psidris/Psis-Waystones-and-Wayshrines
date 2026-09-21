package com.psi.wsaws.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.psi.wsaws.WSaWS;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = WSaWS.MODID)
public class DataGenerators {
	private DataGenerators() {
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		
		//generate block states, block models, and item models
		gen.addProvider(event.includeServer(), new ModBlockStateProvider(gen.getPackOutput(), existingFileHelper));
		gen.addProvider(event.includeServer(), new ModItemModelProvider(gen.getPackOutput(), existingFileHelper));
		
		//generate recipes
		gen.addProvider(event.includeServer(), new ModRecipeProvider(gen.getPackOutput(), lookupProvider));
		//generate block tags
		gen.addProvider(event.includeServer(), new ModBlockTagProvider(gen.getPackOutput(), lookupProvider, existingFileHelper));
		//generate loot tables
		gen.addProvider(event.includeServer(), new ModLootTableProvider(gen.getPackOutput(), lookupProvider));
		
		//generate advancements
		gen.addProvider(event.includeServer(), new ModAdvancementsProvider(gen.getPackOutput(), lookupProvider, List.of(new ModAdvancementsSubProvider())));
	}
}
