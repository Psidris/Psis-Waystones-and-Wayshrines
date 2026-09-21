package com.psi.wsaws.datagen;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, WSaWS.MODID, existingFileHelper);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void registerModels() {
		
		ModelFile ItemGenerated = getExistingFile(mcLoc("item/generated"));
		
		itemBuilder(ItemGenerated, "everlasting_ender_pearl");
		itemBuilder(ItemGenerated, "crystal_shell");
		itemBuilder(ItemGenerated, "waystone_core");
		itemBuilder(ItemGenerated, "waystone_block_deepslate");
		itemBuilder(ItemGenerated, "resonance_crystal_shard");
		itemBuilder(ItemGenerated, "rift_claw");
		
		blockModel(BlockInit.RESONANCE_CRYSTAL_BLOCK);	
		blockModel(BlockInit.BUDDING_RESONANCE_CRYSTAL);
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
	
	public void singleLayerBlockModel(Supplier<? extends Block> block, ModelFile modelFile) {
        getBuilder(getName(block)).parent(modelFile).texture("layer0", "block/" + getName(block));
    }
	
	String getName(Supplier<? extends Block> block) {
		return block.get().builtInRegistryHolder().key().location().getPath();
	}
}
