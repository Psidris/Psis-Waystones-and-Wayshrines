package com.psi.wsaws.common.entity;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityTypeInit {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, WSaWS.MODID);

	public static final Supplier<EntityType<EverlastingEnderPearlEntity>> EVERLASTING_ENDER_PEARL = register(
			"everlasting_ender_pearl",
			EntityType.Builder.<EverlastingEnderPearlEntity>of(EverlastingEnderPearlEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

	static void register() {
	}

	private static <T extends Entity> Supplier<EntityType<T>> register(String name, Builder<T> builder) {
		return ENTITIES.register(name, () -> builder.build(name));
	}
}
