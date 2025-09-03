package com.psi.wsaws.datagen;

import java.util.function.Consumer;

import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ModAdvancementsSubProvider implements AdvancementSubProvider {

	@Override
	public void generate(Provider provider, Consumer<Advancement> advancements) {
		Advancement root = Advancement.Builder.advancement()
				.display(Items.SPYGLASS.asItem(), Component.translatable("advancements.wsaws.root.title"),
						Component.translatable("advancements.wsaws.root.desc"), new ResourceLocation("textures/gui/advancements/backgrounds/wsaws.png"),
						FrameType.TASK, false, false, false)
				.addCriterion("wsaws/root", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL, Items.AMETHYST_SHARD, Items.CRYING_OBSIDIAN, Items.ECHO_SHARD, Items.CHORUS_FRUIT))
				.save(advancements, "wsaws/root");
		
		Advancement how_curious_chorus_eaten = Advancement.Builder.advancement()
				.parent(root)
				.display(Items.CHORUS_FRUIT.asItem(), Component.translatable("advancements.wsaws.chorus_eaten.title"),
						Component.translatable("advancements.wsaws.chorus_eaten.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("chorus_eaten", ConsumeItemTrigger.TriggerInstance.usedItem(Items.CHORUS_FRUIT))
				.save(advancements, "wsaws/chorus_eaten");
		Advancement find_amethyst_geode = Advancement.Builder.advancement()
				.parent(root)
				.display(Blocks.AMETHYST_CLUSTER.asItem(), Component.translatable("advancements.wsaws.find_amethyst_geode.title"),
						Component.translatable("advancements.wsaws.find_amethyst_geode.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("find_amethyst_geode", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD))
				.save(advancements, "wsaws/find_amethyst_geode");
		Advancement find_quartz = Advancement.Builder.advancement()
				.parent(root)
				.display(Items.QUARTZ.asItem(), Component.translatable("advancements.wsaws.find_quartz.title"),
						Component.translatable("advancements.wsaws.find_quartz.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("find_quartz", InventoryChangeTrigger.TriggerInstance.hasItems(Items.QUARTZ))
				.save(advancements, "wsaws/find_quartz");
		Advancement how_curious_crying_obi = Advancement.Builder.advancement()
				.parent(root)
				.display(Blocks.CRYING_OBSIDIAN.asItem(), Component.translatable("advancements.wsaws.crying_obi.title"),
						Component.translatable("advancements.wsaws.crying_obi.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("traveled_dimensions", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER))
				.addCriterion("crying_obi", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRYING_OBSIDIAN))
				.save(advancements, "wsaws/crying_obi");
		Advancement how_curious_echo_shard = Advancement.Builder.advancement()
				.parent(root)
				.display(Items.ECHO_SHARD.asItem(), Component.translatable("advancements.wsaws.echo_shard.title"),
						Component.translatable("advancements.wsaws.echo_shard.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("echo_shard", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ECHO_SHARD))
				.save(advancements, "wsaws/echo_shard");
		
		Advancement how_curious_ender_pearl = Advancement.Builder.advancement()
				.parent(root)
				.display(Items.ENDER_PEARL.asItem(), Component.translatable("advancements.wsaws.pearl_thrown.title"),
						Component.translatable("advancements.wsaws.pearl_thrown.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("ender_pearl_thrown", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.save(advancements, "wsaws/ender_pearl_thrown");
		Advancement craft_crystal_shell = Advancement.Builder.advancement()
				.parent(how_curious_ender_pearl)
				.display(ItemInit.CRYSTAL_SHELL.get().asItem(), Component.translatable("advancements.wsaws.craft_crystal_shell.title"),
						Component.translatable("advancements.wsaws.craft_crystal_shell.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("craft_crystal_shell", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.CRYSTAL_SHELL.get()))
				.save(advancements, "wsaws/craft_crystal_shell");
		Advancement acquire_nether_star = Advancement.Builder.advancement()
				.parent(craft_crystal_shell)
				.display(Items.NETHER_STAR.asItem(), Component.translatable("advancements.wsaws.acquire_nether_star.title"),
						Component.translatable("advancements.wsaws.acquire_nether_star.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, true)
				.addCriterion("acquire_nether_star", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHER_STAR))
				.save(advancements, "wsaws/acquire_nether_star");
		Advancement craft_perfect_pearl = Advancement.Builder.advancement()
				.parent(acquire_nether_star)
				.display(ItemInit.EVERLASTING_ENDER_PEARL.get().asItem(), Component.translatable("advancements.wsaws.craft_perfect_pearl.title"),
						Component.translatable("advancements.wsaws.craft_perfect_pearl.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, false)
				.addCriterion("craft_perfect_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.EVERLASTING_ENDER_PEARL.get()))
				.save(advancements, "wsaws/craft_perfect_pearl");

		Advancement craft_waystone_core = Advancement.Builder.advancement()
				.parent(how_curious_chorus_eaten)
				.parent(how_curious_crying_obi)
				.parent(how_curious_echo_shard)
				.parent(craft_perfect_pearl)
				.display(ItemInit.WAYSTONE_CORE.get().asItem(), Component.translatable("advancements.wsaws.craft_waystone_core.title"),
						Component.translatable("advancements.wsaws.craft_waystone_core.desc"), (ResourceLocation) null,
						FrameType.TASK, true, true, false)
				.addCriterion("craft_waystone_core", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.WAYSTONE_CORE.get()))
				.save(advancements, "wsaws/craft_waystone_core");
		Advancement craft_waystone = Advancement.Builder.advancement()
				.parent(craft_waystone_core)
				.display(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get().asItem(), Component.translatable("advancements.wsaws.craft_waystone.title"),
						Component.translatable("advancements.wsaws.craft_waystone.desc"), (ResourceLocation) null,
						FrameType.GOAL, true, true, false)
				.addCriterion("craft_waystone", InventoryChangeTrigger.TriggerInstance.hasItems(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get()))
				.save(advancements, "wsaws/craft_waystone");
	}

}
