package com.psi.wsaws.common.item;

import com.psi.wsaws.common.entity.EverlastingEnderPearlEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class EverlastingEnderPearlItem extends Item {

	public EverlastingEnderPearlItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), true);
	}

	@Override
	public void releaseUsing(ItemStack itemstack, Level level, LivingEntity entity, int windup) {
		if(entity instanceof Player) {
			Player player = (Player)entity;
			
			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			player.getCooldowns().addCooldown(this, 20);
			if (!level.isClientSide) {
				EverlastingEnderPearlEntity enderpearlentity = new EverlastingEnderPearlEntity(level, player);
				enderpearlentity.setItem(itemstack);
				float charge = getPowerForTime((getUseDuration(itemstack, entity)-windup))+1;
				enderpearlentity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, charge, 1.0F);
				level.addFreshEntity(enderpearlentity);
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			if (!player.getAbilities().instabuild) {
				itemstack.shrink(1);
			}
		}
	}

	public static float getPowerForTime(int charge) {
	      float f = (float)charge / 20.0F;
	      f = (f * f + f * 2.0F) / 3.0F;
	      if (f > 1F) {
	         f = 1F;
	      }

	      return f;
	   }
	
	

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPEAR;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}
	
	@Override
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}
}
