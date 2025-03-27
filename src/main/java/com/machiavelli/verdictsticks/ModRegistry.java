package com.machiavelli.verdictsticks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/*
  ModRegistry.java is responsible for registering your mod’s custom elements.
  We add our five custom verdict stick items here.
*/
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {

  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MODID);
  public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModMain.MODID);

  // Register verdict sticks (no crafting recipe, obtainable via creative mode or /give)
  public static final RegistryObject<Item> KNIGHT_STICK = ITEMS.register("knight_stick",
          () -> new VerdictStickItem(new Item.Properties(), ConfigManager.knightStickAllowedRank.get()));
  public static final RegistryObject<Item> BARON_STICK = ITEMS.register("baron_stick",
          () -> new VerdictStickItem(new Item.Properties(), ConfigManager.baronStickAllowedRank.get()));
  public static final RegistryObject<Item> COUNT_STICK = ITEMS.register("count_stick",
          () -> new VerdictStickItem(new Item.Properties(), ConfigManager.countStickAllowedRank.get()));
  public static final RegistryObject<Item> KING_STICK = ITEMS.register("king_stick",
          () -> new VerdictStickItem(new Item.Properties(), ConfigManager.kingStickAllowedRank.get()));
  public static final RegistryObject<Item> HIGH_KING_STICK = ITEMS.register("high_king_stick",
          () -> new VerdictStickItem(new Item.Properties(), ConfigManager.highKingStickAllowedRank.get()));
}
