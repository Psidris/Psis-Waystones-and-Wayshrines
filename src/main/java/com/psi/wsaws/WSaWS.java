package com.psi.wsaws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.psi.wsaws.client.ClientModEvents;
import com.psi.wsaws.client.render.ModEntityRendererManager;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.block.blockentity.BlockEntityInit;
import com.psi.wsaws.common.entity.EntityTypeInit;
import com.psi.wsaws.common.item.ItemInit;
import com.psi.wsaws.common.util.CreativeTabInit;
import com.psi.wsaws.common.util.DataComponentTypeInit;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WSaWS.MODID)
public class WSaWS
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "wsaws";
    
    public static Logger LOGGER = LogManager.getLogger(MODID);

    public WSaWS(IEventBus modBus)
    {

        // Register the commonSetup method for modloading
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::doClientStuff);
        
        modBus.register(ClientModEvents.class);
        
        CreativeTabInit.CREATIVE_MODE_TABS.register(modBus);
		BlockInit.BLOCKS.register(modBus);
		ItemInit.ITEMS.register(modBus);
		BlockEntityInit.BLOCK_ENTITY_TYPES.register(modBus);
		EntityTypeInit.ENTITIES.register(modBus);
		DataComponentTypeInit.DATA_COMPONENTS.register(modBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
    	ItemBlockRenderTypes.setRenderLayer(BlockInit.WAYSTONE_BLOCK.get(), RenderType.cutout());
		ModEntityRendererManager.registerRenderers();
	}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
}
