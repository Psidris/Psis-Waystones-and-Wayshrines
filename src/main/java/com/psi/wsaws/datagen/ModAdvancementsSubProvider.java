package com.psi.wsaws.datagen;

import java.util.function.Consumer;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.UsingItemTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.advancements.packs.VanillaAdventureAdvancements;
import net.minecraft.data.advancements.packs.VanillaStoryAdvancements;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ModAdvancementsSubProvider implements AdvancementSubProvider {

	@Override
	public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> advancements) {
		AdvancementHolder root = Advancement.Builder.advancement()
				.display(
						Items.SPYGLASS.asItem(),
						Component.translatable("advancements.wsaws.root.title"),
						Component.translatable("advancements.wsaws.root.desc"),
						ResourceLocation.fromNamespaceAndPath(WSaWS.MODID, "textures/gui/advancements/backgrounds/wsaws.png"),
						AdvancementType.TASK,
						true,
						false,
						false)
				.addCriterion("found_enderpearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.addCriterion("chorus_eaten", ConsumeItemTrigger.TriggerInstance.usedItem(Items.CHORUS_FRUIT))
				.addCriterion("found_amethyst", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD))
				.addCriterion("found_echo_shard", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ECHO_SHARD))
				.addCriterion("found_crying_obi", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRYING_OBSIDIAN))
				.save(advancements, "wsaws/root");


		AdvancementHolder how_curious_ender_pearl = Advancement.Builder.advancement()
				.parent(root)
				.display(Items.ENDER_PEARL.asItem(), Component.translatable("advancements.wsaws.how_curious_ender_pearl.title"),
						Component.translatable("advancements.wsaws.how_curious_ender_pearl.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, false, true)
				.addCriterion("found_enderpearl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENDER_PEARL))
				.save(advancements, "wsaws/how_curious_ender_pearl");

		AdvancementHolder how_curious_amethyst = Advancement.Builder.advancement()
				.parent(root)
				.display(Items.AMETHYST_SHARD, Component.translatable("advancements.wsaws.how_curious_amethyst.title"),
						Component.translatable("advancements.wsaws.how_curious_amethyst.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, false, true)
				.addCriterion("found_amethyst", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD))
				.save(advancements, "wsaws/how_curious_amethyst");

		AdvancementHolder how_curious_echo_shard = Advancement.Builder.advancement()
				.parent(root)
				.display(Items.ECHO_SHARD.asItem(), Component.translatable("advancements.wsaws.how_curious_echo_shard.title"),
						Component.translatable("advancements.wsaws.how_curious_echo_shard.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, false, true)
				.addCriterion("found_echo_shard", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ECHO_SHARD))
				.save(advancements, "wsaws/how_curious_echo_shard");

		AdvancementHolder dissonance = Advancement.Builder.advancement()
				.parent(how_curious_echo_shard)
				.display(Blocks.SCULK_CATALYST,
				Component.translatable("advancements.wsaws.dissonance.title"),
				Component.translatable("advancements.wsaws.dissonance.desc"),
				(ResourceLocation) null,
				AdvancementType.TASK, true, false, true)
				.addCriterion("dissonance", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD, Items.ECHO_SHARD))
				.save(advancements, "wsaws/dissonance");

		AdvancementHolder harmony = Advancement.Builder.advancement()
				.parent(how_curious_amethyst)
				.display(ItemInit.RESONANCE_SHARD.get().asItem(),
				Component.translatable("advancements.wsaws.harmony.title"),
				Component.translatable("advancements.wsaws.harmony.desc"),
				(ResourceLocation) null,
				AdvancementType.TASK, true, true, true)
				.addCriterion("acquire_resonance_shard", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.RESONANCE_SHARD.get()))
				.save(advancements, "wsaws/harmony");

		AdvancementHolder craft_crystal_shell = Advancement.Builder.advancement()
				.parent(how_curious_ender_pearl)
				.display(ItemInit.CRYSTAL_SHELL.get().asItem(), Component.translatable("advancements.wsaws.craft_crystal_shell.title"),
						Component.translatable("advancements.wsaws.craft_crystal_shell.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, false, true)
				.addCriterion("craft_crystal_shell", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.CRYSTAL_SHELL.get()))
				.save(advancements, "wsaws/craft_crystal_shell");

		AdvancementHolder acquire_nether_star = Advancement.Builder.advancement()
				.parent(craft_crystal_shell)
				.display(Items.NETHER_STAR.asItem(), Component.translatable("advancements.wsaws.acquire_nether_star.title"),
						Component.translatable("advancements.wsaws.acquire_nether_star.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, true, true)
				.addCriterion("acquire_nether_star", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHER_STAR))
				.save(advancements, "wsaws/acquire_nether_star");

		AdvancementHolder craft_perfect_pearl = Advancement.Builder.advancement()
				.parent(acquire_nether_star)
				.display(ItemInit.EVERLASTING_ENDER_PEARL.get().asItem(), Component.translatable("advancements.wsaws.craft_perfect_pearl.title"),
						Component.translatable("advancements.wsaws.craft_perfect_pearl.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, true, true)
				.addCriterion("craft_perfect_pearl", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.EVERLASTING_ENDER_PEARL.get()))
				.save(advancements, "wsaws/craft_perfect_pearl");

		AdvancementHolder how_curious_crying_obi = Advancement.Builder.advancement()
				.parent(harmony)
				.display(Blocks.CRYING_OBSIDIAN.asItem(), Component.translatable("advancements.wsaws.how_curious_crying_obi.title"),
						Component.translatable("advancements.wsaws.how_curious_crying_obi.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, false, true)
				.requirements(AdvancementRequirements.Strategy.AND)
				.addCriterion("traveled_dimensions", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER))
				.addCriterion("found_crying_obi", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRYING_OBSIDIAN))
				.save(advancements, "wsaws/how_curious_crying_obi");

		AdvancementHolder how_curious_chorus_eaten = Advancement.Builder.advancement()
				.parent(how_curious_crying_obi)
				.display(Items.CHORUS_FRUIT.asItem(),
						Component.translatable("advancements.wsaws.chorus_eaten.title"),
						Component.translatable("advancements.wsaws.chorus_eaten.desc"),
						(ResourceLocation) null,
						AdvancementType.TASK,
						true,
						true,
						true)
				.addCriterion("chorus_eaten", ConsumeItemTrigger.TriggerInstance.usedItem(Items.CHORUS_FRUIT))
				.save(advancements, "wsaws/chorus_eaten");

		AdvancementHolder craft_rift_core = Advancement.Builder.advancement()
				.parent(how_curious_chorus_eaten)
				.display(BlockInit.RIFT_CORE_BLOCK.get().asItem(), Component.translatable("advancements.wsaws.craft_rift_core.title"),
						Component.translatable("advancements.wsaws.craft_rift_core.desc"), (ResourceLocation) null,
						AdvancementType.TASK, true, true, true)
				.addCriterion("craft_rift_core", InventoryChangeTrigger.TriggerInstance.hasItems(BlockInit.RIFT_CORE_BLOCK.get()))
				.save(advancements, "wsaws/craft_rift_core");

		AdvancementHolder craft_waystone = Advancement.Builder.advancement()
				.parent(craft_rift_core)
				.display(Blocks.ANVIL, Component.translatable("advancements.wsaws.craft_waystone.title"),
						Component.translatable("advancements.wsaws.craft_waystone.desc"), (ResourceLocation) null,
						AdvancementType.GOAL, false, false, true)
				.addCriterion("craft_rift_core", InventoryChangeTrigger.TriggerInstance.hasItems(BlockInit.RIFT_CORE_BLOCK.get()))
				.save(advancements, "wsaws/craft_waystone");
	}

}
