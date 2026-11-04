package com.psi.wsaws.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTabInit {

	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WSaWS.MODID);

	public static final List<Supplier<? extends ItemLike>> CREATIVE_TAB_ITEMS = new ArrayList<>();

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WSAWS_CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.register("tab_wsaws",
			() -> CreativeModeTab.builder().title(Component.translatable("tab.tab_wsaws"))
					.icon(ItemInit.EVERLASTING_ENDER_PEARL.get()::getDefaultInstance)
					.displayItems((displayParameters, output) ->
						CREATIVE_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
					.build());

	public static <T extends Item> DeferredHolder<Item, T> addToTab(DeferredHolder<Item, T> itemLike) {
		CREATIVE_TAB_ITEMS.add(itemLike);
		return itemLike;
	}
}
