package com.psi.wsaws.client;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.psi.wsaws.WSaWS;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class WaystoneGeometryLoader implements IGeometryLoader<WaystonePartGeometry> {
	
	public static final WaystoneGeometryLoader INSTANCE = new WaystoneGeometryLoader();
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(WSaWS.MODID, "waystone_model_loader");
	
	private WaystoneGeometryLoader() {}

	@Override
	public WaystonePartGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
		
		Map<String, ResourceLocation> parts = new LinkedHashMap<>();
		
		for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			if(entry.getKey().equals("loader")) continue;
			parts.put(entry.getKey(), ResourceLocation.parse(entry.getValue().getAsString()));
		}
		
		return new WaystonePartGeometry(parts);
	}
	
		

}
