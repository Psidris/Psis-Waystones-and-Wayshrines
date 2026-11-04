package com.psi.wsaws.datagen;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, WSaWS.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		
		ModelFile ItemGenerated = getExistingFile(mcLoc("item/generated"));
		
		itemBuilder(ItemGenerated, "everlasting_ender_pearl");
		itemBuilder(ItemGenerated, "crystal_shell");
		itemBuilder(ItemGenerated, "resonance_crystal_shard");
		itemBuilder(ItemGenerated, "rift_claw");
		
		//simpleBlockItem(BlockInit.RIFT_CORE_BLOCK.get());
		blockModel(BlockInit.RIFT_CORE_BLOCK);

		resonanceCrystalBlock(BlockInit.RESONANCE_CRYSTAL_BLOCK);
		resonanceCrystalBlock(BlockInit.BUDDING_RESONANCE_CRYSTAL);
		singleLayerBlockModel(BlockInit.RESONANCE_CRYSTAL_CLUSTER, ItemGenerated);
		singleLayerBlockModel(BlockInit.LARGE_RESONANCE_CRYSTAL_BUD, ItemGenerated);
		singleLayerBlockModel(BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD, ItemGenerated);
		singleLayerBlockModel(BlockInit.SMALL_RESONANCE_CRYSTAL_BUD, ItemGenerated);
	}
	
	private ItemModelBuilder itemBuilder(ModelFile itemGenerated, String name) {
		return getBuilder(name).parent(itemGenerated).texture("layer0","item/"+name);
	}
	
	private void blockModel(Supplier<? extends Block> block) {
        withExistingParent(getName(block), modLoc("block/" + getName(block)));
    }

	private void resonanceCrystalBlock(Supplier<? extends Block> block) {
		withExistingParent(getName(block), modLoc("block/" + getName(block) + "/0"));
	}
	
	public void singleLayerBlockModel(Supplier<? extends Block> block, ModelFile modelFile) {
        getBuilder(getName(block)).parent(modelFile).texture("layer0", "block/" + getName(block) + "/0");
    }
	
	String getName(Supplier<? extends Block> block) {
		return block.get().builtInRegistryHolder().key().location().getPath();
	}
}
