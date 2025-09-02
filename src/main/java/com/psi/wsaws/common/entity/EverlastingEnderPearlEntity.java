package com.psi.wsaws.common.entity;

import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EverlastingEnderPearlEntity extends ThrowableItemProjectile {
	
	public EverlastingEnderPearlEntity(EntityType<? extends EverlastingEnderPearlEntity> type, Level level) {
		super(type, level);
	}

	public EverlastingEnderPearlEntity(Level level, LivingEntity entity) {
		super(EntityTypeInit.EVERLASTING_ENDER_PEARL.get(), entity, level);
	}

	public EverlastingEnderPearlEntity(double x, double y, double z, Level level) {
		super(EntityTypeInit.EVERLASTING_ENDER_PEARL.get(), x, y, z, level);
	}

	protected Item getDefaultItem() {
		return ItemInit.EVERLASTING_ENDER_PEARL.get();
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		super.tick();
		Vec3 vec3 = this.getDeltaMovement();
		double d0 = this.getX() + vec3.x;
		double d1 = this.getY() + vec3.y;
		double d2 = this.getZ() + vec3.z;
		double d3 = vec3.horizontalDistance();
		this.setXRot(Projectile.lerpRotation(this.xRotO, (float) (Mth.atan2(vec3.y, d3) * (double) (180F / (float) Math.PI))));
		this.setYRot(Projectile.lerpRotation(this.yRotO, (float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI))));
		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D,
						d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
			}
		} else {
			this.level().addParticle(ParticleTypes.PORTAL, d0 - vec3.x * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, d1 - vec3.y * 0.25D - 0.5D, d2 - vec3.z * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, vec3.x, vec3.y, vec3.z);
		}
	}

	@Override
	protected void onHit(HitResult hit) {
		super.onHit(hit);
		Entity entity = this.getOwner();

		if (!this.level().isClientSide && !this.isRemoved()) {
			if (entity instanceof ServerPlayer) {
				ServerPlayer serverplayer = (ServerPlayer) entity;
				if (serverplayer.connection.isAcceptingMessages() && serverplayer.level() == this.level()
						&& !serverplayer.isSleeping()) {
					net.minecraftforge.event.entity.EntityTeleportEvent.EnderPearl event = net.minecraftforge.event.ForgeEventFactory.onEnderPearlLand(serverplayer, this.getX(), this.getY(), this.getZ(), null, 0.0f, hit);
					if (!event.isCanceled()) {
						if (this.random.nextFloat() < 0.05F
								&& this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
							Endermite endermite = EntityType.ENDERMITE.create(this.level());
							if (endermite != null) {
								endermite.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(),
										entity.getXRot());
								this.level().addFreshEntity(endermite);
							}
						}

						if (entity.isPassenger()) {
							entity.stopRiding();
						}

						entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
						entity.resetFallDistance();
					} // Forge: End
				}
			} else if (entity != null) {
				entity.teleportTo(this.getX(), this.getY(), this.getZ());
				entity.resetFallDistance();
			}
			// drop item
			if (!((ServerPlayer) entity).getAbilities().instabuild) {
				if(this.level().addFreshEntity(new ItemEntity(this.level(), (double) this.getX(), (double) this.getY(), (double) this.getZ(), this.getDefaultItem().getDefaultInstance()))) {
				}
			}
			
			for(int i = 0; i<5; i++) {
				this.level().levelEvent(2003, this.blockPosition(), 0);
			}

			this.discard();

		}

	}

}