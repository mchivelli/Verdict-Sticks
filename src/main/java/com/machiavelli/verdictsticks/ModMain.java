package com.machiavelli.verdictsticks;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModMain.MODID)
public class ModMain {

  public static final String MODID = "verdictsticks";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModMain() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

    ModRegistry.BLOCKS.register(eventBus);
    ModRegistry.ITEMS.register(eventBus);
    ModRegistry.TILE_ENTITIES.register(eventBus);

    // Load the configuration file here to ensure config values are available.
    ConfigManager.loadConfig(ConfigManager.CONFIG, MODID + ".toml");

    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
  }

  private void setup(final FMLCommonSetupEvent event) {
    // Common setup logic here
  }

  private void setupClient(final FMLClientSetupEvent event) {
    // Client-only setup logic here
  }

  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    LOGGER.info("Server starting: Initializing Mod");
  }

  @SubscribeEvent
  public void onRegisterCommands(RegisterCommandsEvent event) {
    // Register commands here if needed
  }
}
