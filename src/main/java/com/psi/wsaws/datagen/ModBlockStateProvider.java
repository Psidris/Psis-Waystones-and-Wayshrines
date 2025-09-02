package com.psi.wsaws.datagen;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.block.WaystoneBlock;
import com.psi.wsaws.common.block.WaystoneCoreBlock;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(PackOutput gen, ExistingFileHelper existingFileHelper) {
		super(gen, WSaWS.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		
		//horizontalBlock(BlockInit.WAYSTONE_BLOCK.get(), models().getExistingFile(modLoc("block/waystone/waystone_block")));
		
		waystoneBlock(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get(), "waystone_block");
	}
	
	@SuppressWarnings("static-access")
	public void waystoneBlock(WaystoneBlock block, String name) {
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
}