package com.psi.wsaws.client.render;

import com.psi.wsaws.common.entity.EntityTypeInit;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ModEntityRendererManager {
	
	public ModEntityRendererManager() {
	}

	public static void registerRenderers() {
		EntityRenderers.register(EntityTypeInit.EVERLASTING_ENDER_PEARL.get(), ThrownItemRenderer::new);
	}
}
