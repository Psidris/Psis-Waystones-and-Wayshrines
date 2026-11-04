package com.psi.wsaws.datagen;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.errorprone.annotations.Var;
import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.block.ResonanceCrystalBlock;
import com.psi.wsaws.common.block.ResonanceCrystalClusterBlock;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
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

		buildResonanceCrystals(BlockInit.RESONANCE_CRYSTAL_BLOCK);
		buildResonanceCrystals(BlockInit.BUDDING_RESONANCE_CRYSTAL);
		buildResonanceClusters(List.of(
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
	private <T extends ResonanceCrystalBlock> void buildResonanceCrystals(DeferredHolder<Block, T> block) {
		VariantBlockStateBuilder builder = getVariantBuilder(block.get());
		builder.forAllStates(state -> {
			int phase = state.getValue(ResonanceCrystalBlock.PHASE);
			ModelFile model = models().cubeAll(
					"block/" + getName(block) + "/" + phase,
					ResourceLocation.fromNamespaceAndPath(
							WSaWS.MODID,
							"block/" + getName(block) + "/" + phase));
			Function<BlockState, ModelFile> modelFunc = $ -> model;
			return ConfiguredModel.builder()
					.modelFile(modelFunc.apply(state))
					.build();
		});
	}

	private void buildResonanceClusters(List<DeferredHolder<Block, ResonanceCrystalClusterBlock>> list) {
		for(DeferredHolder<Block, ResonanceCrystalClusterBlock> block : list){
			VariantBlockStateBuilder builder = getVariantBuilder(block.get());
			builder.forAllStates(state -> {
				int phase = state.getValue(ResonanceCrystalClusterBlock.PHASE);
				Direction dir = state.getValue(BlockStateProperties.FACING);
				ModelFile model = models().cross(
						"block/" + getName(block) + "/" + phase,
						ResourceLocation.fromNamespaceAndPath(
								WSaWS.MODID,
								"block/" + getName(block) + "/" + phase))
						.renderType("cutout");
				Function<BlockState, ModelFile> modelFunc = $ -> model;
				return ConfiguredModel.builder()
						.modelFile(modelFunc.apply(state))
						.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
						.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
						.build();
			});
		}
	}
	
	private String getName(Supplier<? extends Block> block) {
		return block.get().builtInRegistryHolder().key().location().getPath();
	}
}