package com.psi.wsaws.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockTagProvider extends BlockTagsProvider {

	public ModBlockTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, WSaWS.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider p_256380_) {
		//mine with pickaxe
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				BlockInit.WAYSTONE_BLOCK.get(),
				
				BlockInit.RESONANCE_CRYSTAL_BLOCK.get(),
				BlockInit.RESONANCE_CRYSTAL_CLUSTER.get(),
				BlockInit.LARGE_RESONANCE_CRYSTAL_BUD.get(),
				BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD.get(),
				BlockInit.SMALL_RESONANCE_CRYSTAL_BUD.get());
		
		//tool level
		this.tag(BlockTags.NEEDS_STONE_TOOL).add(
				BlockInit.RESONANCE_CRYSTAL_BLOCK.get(),
				BlockInit.RESONANCE_CRYSTAL_CLUSTER.get(),
				BlockInit.LARGE_RESONANCE_CRYSTAL_BUD.get(),
				BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD.get(),
				BlockInit.SMALL_RESONANCE_CRYSTAL_BUD.get());
		
		this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
				BlockInit.WAYSTONE_BLOCK.get());
		
		//crystal sound tag
		this.tag(BlockTags.CRYSTAL_SOUND_BLOCKS).add(
				BlockInit.RESONANCE_CRYSTAL_BLOCK.get(),
				BlockInit.BUDDING_RESONANCE_CRYSTAL.get());
		
		//vibration resonators
		this.tag(BlockTags.VIBRATION_RESONATORS).add(
				BlockInit.RESONANCE_CRYSTAL_BLOCK.get());
	}

}
