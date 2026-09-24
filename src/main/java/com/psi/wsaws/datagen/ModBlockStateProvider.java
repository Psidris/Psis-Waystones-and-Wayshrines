package com.psi.wsaws.datagen;

import java.util.List;
import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.block.ResonanceCrystalClusterBlock;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(PackOutput gen, ExistingFileHelper existingFileHelper) {
		super(gen, WSaWS.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		
		//horizontalBlock(BlockInit.WAYSTONE_BLOCK.get(), models().getExistingFile(modLoc("block/waystone/waystone_block")));
		
		waystoneBlock(BlockInit.WAYSTONE_BLOCK.get(), "waystone_block");
		
		horizontalBlock(BlockInit.RIFT_CORE_BLOCK.get(), models().getExistingFile(modLoc("block/rift_core_block")));
		
		simpleBlock(BlockInit.RESONANCE_CRYSTAL_BLOCK.get());
		simpleBlock(BlockInit.BUDDING_RESONANCE_CRYSTAL.get());
		buildResonanceCrystals(List.of(
				BlockInit.RESONANCE_CRYSTAL_CLUSTER,
				BlockInit.LARGE_RESONANCE_CRYSTAL_BUD,
				BlockInit.MEDIUM_RESONANCE_CRYSTAL_BUD,
				BlockInit.SMALL_RESONANCE_CRYSTAL_BUD));
	}
	
	private void waystoneBlock(Block block, String name) {
		ExistingModelFile lower = models().getExistingFile(modLoc("block/waystone/waystone_block_lower"));
		ExistingModelFile upper = models().getExistingFile(modLoc("block/waystone/waystone_block_upper"));
		
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        builder.forAllStates(state -> {
            boolean islower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
            
            ModelFile model = islower ? lower : upper;
            
        	return ConfiguredModel.builder()
        			.modelFile(model)
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
        			.build();
        });
    }
	
	private void buildResonanceCrystals(List<DeferredHolder<Block, ResonanceCrystalClusterBlock>> list) {
		for(DeferredHolder<Block, ResonanceCrystalClusterBlock> block : list){
	        directionalBlock(block.get(), models().cross(getName(block), ResourceLocation.fromNamespaceAndPath(WSaWS.MODID, "block/" + getName(block))).renderType("cutout"));
		}
	}
	
	private String getName(Supplier<? extends Block> block) {
		return block.get().builtInRegistryHolder().key().location().getPath();
	}
}