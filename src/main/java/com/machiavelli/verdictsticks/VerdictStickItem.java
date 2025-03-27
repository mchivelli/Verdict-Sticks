package com.machiavelli.verdictsticks;

import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import dev.ftb.mods.ftbranks.api.Rank;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class VerdictStickItem extends Item {

    // Allowed power value; players with maximum rank power greater than this are immune.
    private final int allowedRank;

    // Cooldown map for players using a verdict stick (keyed by player UUID, value is last use time in millis)
    private static final Map<UUID, Long> cooldownMap = new HashMap<>();

    public VerdictStickItem(Properties properties, int allowedRank) {
        super(properties);
        this.allowedRank = allowedRank;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        // Only proceed if target is a ServerPlayer and we're on the server side.
        if (!(target instanceof ServerPlayer) || player.getCommandSenderWorld().isClientSide()) {
            return InteractionResult.PASS;
        }
        ServerPlayer targetPlayer = (ServerPlayer) target;
        ServerPlayer playerUsingStick = (ServerPlayer) player;

        // Check cooldown.
        int cooldownMinutes = ConfigManager.stickCooldown.get();
        if (cooldownMinutes > 0) {
            long currentTime = System.currentTimeMillis();
            UUID userId = playerUsingStick.getUUID();
            if (cooldownMap.containsKey(userId)) {
                long lastUsed = cooldownMap.get(userId);
                if (currentTime < lastUsed + cooldownMinutes * 60 * 1000L) {
                    player.sendSystemMessage(Component.literal("You are on cooldown! Please wait before using another verdict stick."));
                    return InteractionResult.FAIL;
                }
            }
        }

        // Retrieve the target player's ranks and determine maximum power.
        Set<Rank> ranks = FTBRanksAPI.manager().getAddedRanks(targetPlayer.getGameProfile());
        int targetPower = ranks.stream().mapToInt(Rank::getPower).max().orElse(0);
        if (targetPower > this.allowedRank) {
            player.sendSystemMessage(Component.literal("You cannot banish that player!"));
            return InteractionResult.FAIL;
        }

        // Calculate random teleport coordinates on the Nether roof.
        int area = ConfigManager.teleportArea.get();
        int netherY = ConfigManager.netherRoofY.get();
        RandomSource rand = RandomSource.create();
        int halfArea = area / 2;
        int x = rand.nextInt(area) - halfArea;
        int z = rand.nextInt(area) - halfArea;

        // Get the Nether level.
        ServerLevel nether = player.getServer().getLevel(Level.NETHER);
        if (nether == null) {
            player.sendSystemMessage(Component.literal("Nether dimension not found!"));
            return InteractionResult.FAIL;
        }

        // Teleport the target player.
        targetPlayer.teleportTo(nether, x, netherY, z, targetPlayer.getYRot(), targetPlayer.getXRot());

        // Broadcast a global banishment message.
        int duration = ConfigManager.banishmentDuration.get();
        Component message = Component.literal(targetPlayer.getName().getString() + " has been Banished for " + duration + " seconds")
                .withStyle(ChatFormatting.RED);
        player.getServer().getPlayerList().broadcastSystemMessage(message, false);

        // Record cooldown.
        if (cooldownMinutes > 0) {
            cooldownMap.put(playerUsingStick.getUUID(), System.currentTimeMillis());
        }

        // Consume the stick.
        stack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}
