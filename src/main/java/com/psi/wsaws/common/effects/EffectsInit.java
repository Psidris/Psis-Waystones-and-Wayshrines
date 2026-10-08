package com.psi.wsaws.common.effects;

import com.psi.wsaws.WSaWS;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.awt.*;
import java.util.function.Supplier;

public class EffectsInit {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, WSaWS.MODID);

    public static final Holder<MobEffect> WAYSHRINE_FOG_EFFECT = EFFECTS.register("wayshrine_fog_effect",
            () -> new WayshrineFog(MobEffectCategory.NEUTRAL, 2696993).setBlendDuration(22));

    private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, Supplier<T> effect) {
        return EFFECTS.register(name, effect);
    }
}
