package com.psi.wsaws.client.render.waystonegeometry;

import java.util.*;

import com.psi.wsaws.WSaWS;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import org.jetbrains.annotations.Nullable;

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
	
	private static final Map<String, ModelProperty<BlockState>> SPRITE_PROPERTIES = Map.of(
				"top", WaystoneBlockTopEntity.TOP_MODEL,
				"bottom", WaystoneBlockEntity.BOTTOM_MODEL,
				"core", WaystoneBlockEntity.CORE_MODEL
			);
	private final Map<String, BakedModel> parts;

	public WaystoneBlockBakedModel(Map<String, BakedModel> baked, TextureAtlasSprite particle,
			boolean useAmbientOcclusion, boolean gui3d2, boolean useBlockLight, ItemOverrides overrides2) {
				this.parts = baked;
				this.particle = particle;
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
	public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
		return ChunkRenderTypeSet.of(RenderType.cutout());
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
		if(state == null){
			return Collections.emptyList();
		}else{
			List<BakedQuad> quads = new ArrayList<>();
			//WSaWS.LOGGER.debug("getting quads for state: {}", state);
			for(var entry : parts.entrySet()) {
				//get model and quads for the part
				List<BakedQuad> templateQuads = entry.getValue().getQuads(state, side, rand, extraData, renderType);

				BlockState storedState = extraData.get(SPRITE_PROPERTIES.get(entry.getKey()));
				if(storedState == null || storedState.isAir()){
					quads.addAll(templateQuads);
					continue;
				}

				Map<Direction, TextureAtlasSprite> newSprites = resolveSprites(storedState);

				for (BakedQuad quad : templateQuads) {
					quads.add(retexture(quad, newSprites.get(quad.getDirection())));
				}
			}

			return quads;
		}
	}

	@Override
	public TextureAtlasSprite getParticleIcon(ModelData data) {
		for (var entry : parts.entrySet()) {
			BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(data.get(SPRITE_PROPERTIES.get(entry.getKey())));
			if(model == null) continue;
			return model.getParticleIcon(data);
		}

		return getParticleIcon();
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

	private static Map<Direction, TextureAtlasSprite> resolveSprites(BlockState state){
		BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
		TextureAtlasSprite fallback = model.getParticleIcon(ModelData.EMPTY);
		RandomSource rand = RandomSource.create(42L);
		Map<Direction, TextureAtlasSprite> sprites = new EnumMap<>(Direction.class);

		for (Direction direction : Direction.values()) {
			TextureAtlasSprite sprite = fallback;
			try {
                List<BakedQuad> quads = model.getQuads(state, direction, rand, ModelData.EMPTY, RenderType.cutout());
				if (!quads.isEmpty()) {
					sprite = quads.get(0).getSprite();
				}
            } catch (RuntimeException e) {
				WSaWS.LOGGER.warn("Failed to resolve {} face sprite for {}", direction, state, e);
			}
			sprites.put(direction, sprite);
		}
		return sprites;
	}
}
