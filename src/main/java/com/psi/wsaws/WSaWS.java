package com.psi.wsaws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.psi.wsaws.client.render.ModEntityRendererManager;
import com.psi.wsaws.common.CreativeTabInit;
import com.psi.wsaws.common.block.BlockInit;
import com.psi.wsaws.common.block.blockentity.BlockEntityInit;
import com.psi.wsaws.common.entity.EntityTypeInit;
import com.psi.wsaws.common.item.ItemInit;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WSaWS.MODID)
public class WSaWS
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "wsaws";
    
    public static Logger LOGGER = LogManager.getLogger(MODID);

    public WSaWS()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::doClientStuff);
        
        CreativeTabInit.CREATIVE_MODE_TABS.register(modEventBus);
		BlockInit.BLOCKS.register(modEventBus);
		ItemInit.ITEMS.register(modEventBus);
		BlockEntityInit.BLOCK_ENTITY_TYPES.register(modEventBus);
		EntityTypeInit.ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
    	ItemBlockRenderTypes.setRenderLayer(BlockInit.WAYSTONE_BLOCK_DEEPSLATE.get(), RenderType.cutout());
		ModEntityRendererManager.registerRenderers();
	}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
}
