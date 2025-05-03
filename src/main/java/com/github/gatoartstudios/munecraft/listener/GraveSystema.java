package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.helpers.PlayerHelper;
import com.github.gatoartstudios.munecraft.helpers.Utils;
import com.github.gatoartstudios.munecraft.models.GraveModel;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * Listener class for handling player death events and managing grave system.
 */
public class GraveSystema implements Listener {

    private final Munecraft plugin;

    /**
     * Constructor for GraveSystema.
     *
     * @param plugin The instance of the Munecraft plugin.
     */
    public GraveSystema(Munecraft plugin) {
        this.plugin = plugin;
    }

    /**
     * Event handler for player death events.
     *
     * @param event The player death event.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String inventory = PlayerHelper.serializeInventory(player);

        // Check permissions and dimension
        boolean canSaveInventory = player.hasPermission("munecraft.grave.saveinventory") && player.getWorld().getName().equals("allowed_dimension");
        boolean canRespawnHere = player.hasPermission("munecraft.grave.respawnhere");
        int duration = canRespawnHere ? getDurationFromPermission(player) : 2;

        // Create Grave in database

        if (plugin.getPlayerDAO().read(player.getUniqueId()) != null) {
            plugin.getPlayerDAO().create(
                    new PlayerModel(
                            player.getUniqueId(),
                            player.getName(),
                            player.getAddress().getAddress().getHostAddress(),
                            null,
                            null
                    )
            );
        }

        plugin.getGraveDAO().create(
                new GraveModel(
                        0,
                        player.getUniqueId(),
                        player.hasPermission("munecraft.grave.respawnhere"),
                        player.hasPermission("munecraft.grave.public"),
                        getDurationFromPermission(player),
                        PlayerHelper.serializeLocation(player.getLocation()),
                        PlayerHelper.serializeInventory(player)
                )
        );

        // Notify player about death and grave details
        player.sendMessage("§cYou have died! Your grave has been created.");
        player.sendMessage("§eGrave Location: X " + player.getLocation().getX() + " Y: " + player.getLocation().getY() + " Z: " + player.getLocation().getZ());
        player.sendMessage("§eGrave Duration: " + getDurationFromPermission(player) + " hours");

        // Handle respawn logic
        handleRespawn(player, canRespawnHere);
    }

    /**
     * Extracts the grave duration from the player's permissions.
     *
     * @param player The player whose permissions are checked.
     * @return The duration of the grave in hours.
     */
    private int getDurationFromPermission(Player player) {
        // Extract duration from permission
        // Example: "munecraft.grave.duration.5" means 5 hours
        final int defaultDuration = 2;

        for (PermissionAttachmentInfo permissionInfo : player.getEffectivePermissions()) {
            String permission = permissionInfo.getPermission();
            if (permission.startsWith("munecraft.grave.duration.")) {
                String durationString = permission.substring("munecraft.grave.duration.".length());
                try {
                    return Integer.parseInt(durationString);
                } catch (NumberFormatException e) {
                    // Ignore
                    plugin.getLogger().warning("Failed to parse grave duration from permission: " + permission);
                }
            }
        }
        return defaultDuration; // Return default if no valid permission is found
    }

    /**
     * Handles the respawn logic for the player.
     *
     * @param player The player to respawn.
     * @param canRespawnHere Whether the player can respawn at the death location.
     */
    private void handleRespawn(Player player, boolean canRespawnHere) {
        Location respawnLocation = canRespawnHere ? player.getLocation() : player.getBedSpawnLocation();
        if (respawnLocation == null) {
            respawnLocation = player.getWorld().getSpawnLocation();
        }

        player.setGameMode(GameMode.SPECTATOR);
        Location finalRespawnLocation = respawnLocation;

        if (Utils.isFolia()) {
            plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> {
                player.teleportAsync(finalRespawnLocation).thenRun(() -> player.setGameMode(GameMode.SURVIVAL));
            }, 200L); // 10 seconds delay
        } else {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.teleport(finalRespawnLocation);
                player.setGameMode(GameMode.SURVIVAL);
            }, 200L); // 10 seconds delay
        }
    }
}
