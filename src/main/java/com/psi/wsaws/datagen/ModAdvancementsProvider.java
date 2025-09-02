package com.psi.wsaws.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;

public class ModAdvancementsProvider extends AdvancementProvider {

	public ModAdvancementsProvider(PackOutput pack, CompletableFuture<Provider> provider, List<AdvancementSubProvider> list) {
		super(pack, provider, list);
		
		
	}
	
	
}
