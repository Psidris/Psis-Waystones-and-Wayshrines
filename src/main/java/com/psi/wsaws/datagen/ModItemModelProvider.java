package com.psi.wsaws.datagen;

import com.psi.wsaws.WSaWS;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, WSaWS.MODID, existingFileHelper);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void registerModels() {
		//waystoneBlockBuilder("waystone_block");
		
		ModelFile ItemGenerated = getExistingFile(mcLoc("item/generated"));
		
		itemBuilder(ItemGenerated, "everlasting_ender_pearl");
		itemBuilder(ItemGenerated, "crystal_shell");
		itemBuilder(ItemGenerated, "waystone_block");
		
	}
	
	public void waystoneBlockBuilder(String name) {
		withExistingParent(name, modLoc("block/waystone/"+name+"_empty"));
	}
	
	private ItemModelBuilder itemBuilder(ModelFile itemGenerated, String name) {
		return getBuilder(name).parent(itemGenerated).texture("layer0","item/"+name);
	}
	
}
