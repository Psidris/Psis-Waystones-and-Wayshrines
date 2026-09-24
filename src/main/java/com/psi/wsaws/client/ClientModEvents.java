package com.psi.wsaws.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

public class ClientModEvents {
	@SubscribeEvent
	public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register(WaystoneGeometryLoader.ID, WaystoneGeometryLoader.INSTANCE);
	}

}
