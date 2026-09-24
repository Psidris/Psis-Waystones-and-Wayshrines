package com.psi.wsaws.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

public record WaystonePartGeometry(Map<String, ResourceLocation> parts) implements IUnbakedGeometry<WaystonePartGeometry>{

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		
		Map<String, BakedModel> baked = new LinkedHashMap<>();
		parts.forEach((name,loc) -> baked.put(name, baker.bake(loc, modelState)));
		
		TextureAtlasSprite particle = spriteGetter.apply(context.getMaterial("particle"));
		
		return new WaystoneBlockBakedModel(baked, particle, context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(), overrides);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		parts.values().forEach(loc -> modelGetter.apply(loc).resolveParents(modelGetter));
	}
	
	
}
