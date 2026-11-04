package com.psi.wsaws.common.util;

import com.psi.wsaws.WSaWS;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;

public class ChunkHandler {
	
	public static void registerChunkTicket(ServerLevel level, BlockPos pos){
		level.getChunkSource().addRegionTicket(TicketType.PORTAL, level.getChunk(pos).getPos(), 0, pos);
		//net.minecraftforge.common.world.ForgeChunkManager.forceChunk(level, WSaWS.MODID, pos, pos.getX(), pos.getZ(), true, true);
	}
	public static void releaseChunkTicket(ServerLevel level, BlockPos pos){
		level.getChunkSource().removeRegionTicket(TicketType.PORTAL, level.getChunk(pos).getPos(), 0, pos);
		//net.minecraftforge.common.world.ForgeChunkManager.forceChunk(level, WSaWS.MODID, pos, pos.getX(), pos.getZ(), false, true);
	}
}
