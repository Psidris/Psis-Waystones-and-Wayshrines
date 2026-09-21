package com.psi.wsaws.common.util;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.psi.wsaws.WSaWS;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentTypeInit {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, WSaWS.MODID);
	
	public static final Codec<WaystoneLinkedPos> BASIC_CODEC = RecordCodecBuilder.create(instance ->
	    instance.group(Codec.LONG.fieldOf("pos").forGetter(WaystoneLinkedPos::pos)).apply(instance, WaystoneLinkedPos::new)
	);
	
	public static final StreamCodec<ByteBuf, WaystoneLinkedPos> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_LONG, WaystoneLinkedPos::pos, WaystoneLinkedPos::new
	);
	
	public static final Supplier<DataComponentType<WaystoneLinkedPos>> WAYSTONE_LINKED_POS = DATA_COMPONENTS.registerComponentType(
			"waystone_linked_pos",
			builder -> builder.persistent(BASIC_CODEC).networkSynchronized(STREAM_CODEC));
}