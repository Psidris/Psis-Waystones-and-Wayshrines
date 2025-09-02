package com.psi.wsaws.common.entity;

import com.psi.wsaws.WSaWS;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeInit {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
			WSaWS.MODID);

	public static final RegistryObject<EntityType<EverlastingEnderPearlEntity>> EVERLASTING_ENDER_PEARL = register(
			"everlasting_ender_pearl",
			EntityType.Builder.<EverlastingEnderPearlEntity>of(EverlastingEnderPearlEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

	static void register() {
	}

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Builder<T> builder) {
		return ENTITIES.register(name, () -> builder.build(name));
	}
}
