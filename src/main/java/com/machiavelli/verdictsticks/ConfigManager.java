package com.machiavelli.verdictsticks;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

import static net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR;

@Mod.EventBusSubscriber
public class ConfigManager {

  public static ForgeConfigSpec CONFIG;
  public static BooleanValue TESTING;

  // New config options for Verdict Sticks
  public static ForgeConfigSpec.IntValue teleportArea;
  public static ForgeConfigSpec.IntValue netherRoofY;
  public static ForgeConfigSpec.IntValue banishmentDuration;
  public static ForgeConfigSpec.IntValue stickCooldown;

  // Allowed rank per stick type (as defined by FTBRanks API; lower numbers mean lower rank)
  public static ForgeConfigSpec.IntValue knightStickAllowedRank;
  public static ForgeConfigSpec.IntValue baronStickAllowedRank;
  public static ForgeConfigSpec.IntValue countStickAllowedRank;
  public static ForgeConfigSpec.IntValue kingStickAllowedRank;
  public static ForgeConfigSpec.IntValue highKingStickAllowedRank;

  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

  static {
    BUILDER.push("Config");
    // Mod settings category
    BUILDER.comment("Mod settings").push(ModMain.MODID);

    // Example option that was here
    TESTING = BUILDER.comment("Testing boolean config").define("doesNothing", true);

    // New configuration options
    teleportArea = BUILDER.comment("Teleportation area size (full width/length in blocks) for the Nether roof")
            .defineInRange("teleportArea", 5000, 100, 10000);
    netherRoofY = BUILDER.comment("Y level for the Nether roof teleport destination")
            .defineInRange("netherRoofY", 127, 1, 256);
    banishmentDuration = BUILDER.comment("Banishment duration in seconds displayed in the global message")
            .defineInRange("banishmentDuration", 60, 1, 3600);
    stickCooldown = BUILDER.comment("Cooldown between stick uses in minutes (0 for none)")
            .defineInRange("stickCooldown", 5, 0, 60);

    knightStickAllowedRank = BUILDER.comment("Allowed rank ID for Knight stick usage")
            .defineInRange("knightStickAllowedRank", 0, 0, 10);
    baronStickAllowedRank = BUILDER.comment("Allowed rank ID for Baron stick usage")
            .defineInRange("baronStickAllowedRank", 1, 0, 10);
    countStickAllowedRank = BUILDER.comment("Allowed rank ID for Count stick usage")
            .defineInRange("countStickAllowedRank", 2, 0, 10);
    kingStickAllowedRank = BUILDER.comment("Allowed rank ID for King stick usage")
            .defineInRange("kingStickAllowedRank", 3, 0, 10);
    highKingStickAllowedRank = BUILDER.comment("Allowed rank ID for High-King stick usage")
            .defineInRange("highKingStickAllowedRank", 4, 0, 10);

    BUILDER.pop(); // pop mod id category
    BUILDER.pop(); // pop Config
    CONFIG = BUILDER.build();
  }

  public static void loadConfig(ForgeConfigSpec config, String path) {
    final Path configPath = CONFIGDIR.get().resolve(path);
    final CommentedFileConfig file = CommentedFileConfig.builder(configPath)
            .sync()
            .autosave()
            .preserveInsertionOrder()
            .build();

    file.load();
    config.setConfig(file);
  }
}
