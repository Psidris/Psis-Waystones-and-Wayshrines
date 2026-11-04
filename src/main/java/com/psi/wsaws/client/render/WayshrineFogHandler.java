package com.psi.wsaws.client.render;

import com.mojang.blaze3d.shaders.FogShape;
import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.effects.EffectsInit;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

public class WayshrineFogHandler {

    static class DarknessFogFunction implements WayshrineFogHandler.MobEffectFogFunction {
        @Override
        public Holder<MobEffect> getMobEffect() {
            return EffectsInit.WAYSHRINE_FOG_EFFECT;
        }

        @Override
        public void setupFog(WayshrineFogHandler.FogData p_234194_, LivingEntity p_234195_, MobEffectInstance p_234196_, float p_234197_, float p_234198_) {
            float f = Mth.lerp(p_234196_.getBlendFactor(p_234195_, p_234198_), p_234197_, 15.0F);
            p_234194_.start = p_234194_.mode == WayshrineFogHandler.FogMode.FOG_SKY ? 0.0F : f * 0.75F;
            p_234194_.end = f;
        }

        @Override
        public float getModifiedVoidDarkness(LivingEntity p_234189_, MobEffectInstance p_234190_, float p_234191_, float p_234192_) {
            return 1.0F - p_234190_.getBlendFactor(p_234189_, p_234192_);
        }
    }

    static class FogData {
        public final WayshrineFogHandler.FogMode mode;
        public float start;
        public float end;
        public FogShape shape = FogShape.SPHERE;

        public FogData(WayshrineFogHandler.FogMode mode) {
            this.mode = mode;
        }
    }

    public static enum FogMode {
        FOG_SKY,
        FOG_TERRAIN;
    }
    interface MobEffectFogFunction {
        Holder<MobEffect> getMobEffect();

        void setupFog(WayshrineFogHandler.FogData fogData, LivingEntity entity, MobEffectInstance effectInstance, float farPlaneDistance, float p_234216_);

        default boolean isEnabled(LivingEntity entity, float p_234207_) {
            return entity.hasEffect(this.getMobEffect());
        }

        default float getModifiedVoidDarkness(LivingEntity entity, MobEffectInstance effectInstance, float p_234210_, float partialTick) {
            MobEffectInstance mobeffectinstance = entity.getEffect(this.getMobEffect());
            if (mobeffectinstance != null) {
                if (mobeffectinstance.endsWithin(19)) {
                    p_234210_ = 1.0F - (float)mobeffectinstance.getDuration() / 20.0F;
                } else {
                    p_234210_ = 0.0F;
                }
            }

            return p_234210_;
        }
    }
}
