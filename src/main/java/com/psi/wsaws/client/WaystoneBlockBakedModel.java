package com.psi.wsaws.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockEntity;
import com.psi.wsaws.common.block.blockentity.WaystoneBlockTopEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class WaystoneBlockBakedModel implements IDynamicBakedModel {
	
	private final TextureAtlasSprite particle;
	private final boolean ao, gui3d, blockLight;
	private final ItemOverrides overrides;
	
	private static final Map<String, ModelProperty<TextureAtlasSprite>> SPRITE_PROPERTIES = Map.of(
				"bottom", WaystoneBlockEntity.BOTTOM_SPRITE,
				"core", WaystoneBlockEntity.CORE_SPRITE,
				"top", WaystoneBlockTopEntity.TOP_SPRITE
			);
	private final Map<String, BakedModel> parts;

	public WaystoneBlockBakedModel(Map<String, BakedModel> baked, TextureAtlasSprite particle2, 
			boolean useAmbientOcclusion, boolean gui3d2, boolean useBlockLight, ItemOverrides overrides2) {
				this.parts = baked;
				this.particle = particle2;
				this.ao = useAmbientOcclusion;
				this.gui3d = gui3d2;
				this.blockLight = useBlockLight;
				this.overrides = overrides2;
	}
	
	@Override public boolean useAmbientOcclusion() { return ao; }
    @Override public boolean isGui3d() { return gui3d; }
    @Override public boolean usesBlockLight() { return blockLight; }
    @Override public TextureAtlasSprite getParticleIcon() { return particle; }
    @Override public ItemOverrides getOverrides() { return overrides; }
    @Override public boolean isCustomRenderer() { return false; }

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
		
		List<BakedQuad> quads = new ArrayList<>();
		
		for(var entry : parts.entrySet()) {
			TextureAtlasSprite sprite = extraData.get(SPRITE_PROPERTIES.get(entry.getKey()));
			for(BakedQuad q : entry.getValue().getQuads(state, side, rand, extraData, renderType)) {
				quads.add(sprite != null ? retexture(q, sprite) : q);
			} //
		}
		
		return quads;
	}
	
	private static BakedQuad retexture(BakedQuad source, TextureAtlasSprite newSprite) {
		//WSaWS.LOGGER.debug("retexture running, setting {} texture to {}", source, newSprite.contents().name());
		int[] original = source.getVertices();
		int[] data = original.clone();
		
		int intsPerVertex = data.length / 4;
		TextureAtlasSprite oldSprite = source.getSprite();
		//WSaWS.LOGGER.info("retexture: intsPerVertex={}, newSprite={} bounds=({},{})-({},{})", intsPerVertex, newSprite.contents().name(), newSprite.getU0(), newSprite.getV0(), newSprite.getU1(), newSprite.getV1());
		for(int vert = 0; vert < 4; vert++) {
			int base = vert * intsPerVertex;
			float u = Float.intBitsToFloat(data[base + 4]);
			float v = Float.intBitsToFloat(data[base + 5]);
			float uFrac = (u - oldSprite.getU0()) / (oldSprite.getU1() - oldSprite.getU0());
			float vFrac = (v - oldSprite.getV0()) / (oldSprite.getV1() - oldSprite.getV0());
	        float newU = newSprite.getU(uFrac);
	        float newV = newSprite.getV(vFrac);
	        //WSaWS.LOGGER.info("  vert={}: uFrac={}, vFrac={} -> newU={}, newV={}", vert, uFrac, vFrac, newU, newV);
			
			data[base + 4] = Float.floatToRawIntBits(newU);
			data[base + 5] = Float.floatToRawIntBits(newV);
			//WSaWS.LOGGER.info("retexture debug: raw u={}, v={}, oldSprite bounds u0={}, u1={}, v0={}, v1={}", u, v, oldSprite.getU0(), oldSprite.getU1(), oldSprite.getV0(), oldSprite.getV1());
		}
		return new BakedQuad(data, source.getTintIndex(), source.getDirection(), newSprite, source.isShade());
	}
	
	private static BakedQuad translate(BakedQuad source, float dx, float dy, float dz) {
	    int[] data = source.getVertices().clone();
	    int intsPerVertex = data.length / 4;
	    for (int vert = 0; vert < 4; vert++) {
	        int base = vert * intsPerVertex;
	        data[base]     = Float.floatToRawIntBits(Float.intBitsToFloat(data[base])     + dx);
	        data[base + 1] = Float.floatToRawIntBits(Float.intBitsToFloat(data[base + 1]) + dy);
	        data[base + 2] = Float.floatToRawIntBits(Float.intBitsToFloat(data[base + 2]) + dz);
	    }
	    return new BakedQuad(data, source.getTintIndex(), source.getDirection(), source.getSprite(), source.isShade());
	}

}
