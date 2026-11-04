package com.psi.wsaws.common.item;

import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.util.CreativeTabInit;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, WSaWS.MODID);

	public static final DeferredHolder<Item, EverlastingEnderPearlItem> EVERLASTING_ENDER_PEARL = register("everlasting_ender_pearl",
			() -> new EverlastingEnderPearlItem(new Item.Properties().fireResistant().stacksTo(1)));
	
	public static final DeferredHolder<Item, CrystalShell> CRYSTAL_SHELL = register("crystal_shell",
			() -> new CrystalShell(new Item.Properties()));
	
	public static final DeferredHolder<Item, ResonanceShard> RESONANCE_SHARD = register("resonance_crystal_shard",
			() -> new ResonanceShard(new Item.Properties()));
	
	public static final DeferredHolder<Item, RiftClaw> RIFT_CLAW = register("rift_claw",
			() -> new RiftClaw(new Item.Properties()));

	static void register() {
	}
	
	private static <T extends Item> DeferredHolder<Item, T> register(String name, Supplier<T> item){
		return CreativeTabInit.addToTab(ITEMS.register(name, item));
	}
}
