package com.psi.wsaws.common.item;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.CreativeTabInit;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WSaWS.MODID);

	public static final RegistryObject<Item> EVERLASTING_ENDER_PEARL = register("everlasting_ender_pearl",
			() -> new EverlastingEnderPearlItem(new Item.Properties().fireResistant().stacksTo(1)));
	public static final RegistryObject<Item> CRYSTAL_SHELL = register("crystal_shell",
			() -> new CrystalShell(new Item.Properties()));
	
	public static final RegistryObject<Item> WAYSTONE_CORE = register("waystone_core",
			() -> new WaystoneCore(new Item.Properties()));
	
	public static final RegistryObject<Item> RESONANCE_SHARD = register("resonance_crystal_shard",
			() -> new ResonanceShard(new Item.Properties()));

	static void register() {
	}
	
	private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item){
		return CreativeTabInit.addToTab(ITEMS.register(name, item));
	}
}
