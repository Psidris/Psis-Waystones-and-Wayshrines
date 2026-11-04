package com.psi.wsaws.common.block.blockentity;

import com.psi.wsaws.WSaWS;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.effects.EffectsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class WayshrineBlockEntity extends BlockEntity {

    public static Map<BlockState, Integer> structure;

    private static boolean enabled = false;

    public WayshrineBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.WAYSHRINE_BLOCK_ENTITY.get(), pos, blockState);
    }

    private static boolean isNearPlayer(Level level, BlockPos pos) {
        return level.hasNearbyAlivePlayer(
                (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0
        );
    }
    public static void clientTick(Level level, BlockPos pos, BlockState state, WayshrineBlockEntity blockEntity) {
        if (!isNearPlayer(level, pos)) { //when no player is near
        } else { //if player is within range
            for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(5, 1, 5))){
                //WSaWS.LOGGER.debug("applying effects to {}", player.getName());

            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WayshrineBlockEntity blockEntity) {
        if(!level.isClientSide()){
            if (isNearPlayer((ServerLevel) level, pos)) { //if player is within range
                //WSaWS.LOGGER.debug("player in range");

                for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(5, 1, 5))){
                    //WSaWS.LOGGER.debug("applying effects to {}", player.getName());

                }
            }
        }
    }
}
