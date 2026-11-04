package com.psi.wsaws.common.item;

import com.psi.wsaws.common.util.DataComponentTypeInit;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class RiftClaw extends Item {

	public RiftClaw(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), true);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
		if(getUseDuration(stack, livingEntity)-timeCharged >=1 && stack.has(DataComponentTypeInit.WAYSTONE_LINKED_POS)) {
			stack.remove(DataComponentTypeInit.WAYSTONE_LINKED_POS);
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.has(DataComponentTypeInit.WAYSTONE_LINKED_POS);
	}
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}
}
