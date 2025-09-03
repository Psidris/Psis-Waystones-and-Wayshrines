package com.psi.wsaws.datagen;

import java.util.List;
import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(PackOutput gen, ExistingFileHelper existingFileHelper) {
		super(gen, WSaWS.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		
		//horizontalBlock(BlockInit.WAYSTONE_BLOCK.get(), models().getExistingFile(modLoc("block/waystone/waystone_block")));
		
		waystoneBlock(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get(), "waystone_block");
		
		String str = getName(BlockInit.BUDDING_RESONANCE_CRYSTAL);
		
		simpleBlock(BlockInit.RESONANCE_CRYSTAL_BLOCK.get());
		simpleBlock(BlockInit.BUDDING_RESONANCE_CRYSTAL.get());
		buildResonanceCrystals(List.of(
				BlockInit.RESONANCE_CRYSTAL_CLUSTER,
				BlockInit.LARGE_RESONANCE_CRYSTAL_BUD,
				BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD,
				BlockInit.SMALL_RESONANCE_CRYSTAL_BUD));
	}
	
	private void waystoneBlock(Block block, String name) {
		ExistingModelFile waystone = models().getExistingFile(modLoc("block/waystone/"+name));
		ExistingModelFile dummy = models().getExistingFile(modLoc("block/waystone/"+name+"_dummy"));
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        builder.forAllStates(state -> {
            boolean lower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
            
            ModelFile model = null;
            if (lower) {
                model = waystone;
            } else if (!lower) {
                model = dummy;
            }
            
        	return ConfiguredModel.builder()
        			.modelFile(model)
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
        			.build();
        });
    }
	
	private void buildResonanceCrystals(List<RegistryObject<Block>> blocks) {
		for(RegistryObject<Block> block : blocks){
	        directionalBlock(block.get(), models().cross(getName(block), new ResourceLocation(WSaWS.MODID, "block/" + getName(block))).renderType("cutout"));
		}
	}
	
	private String getName(Supplier<? extends Block> block) {
		return block.get().builtInRegistryHolder().key().location().getPath();
	}
}