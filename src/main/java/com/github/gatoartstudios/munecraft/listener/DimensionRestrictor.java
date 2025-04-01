package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import java.util.HashMap;
import java.util.UUID;

public class DimensionRestrictor implements Listener {
    private static final String NETHER_PERMISSION = "munecraft.nether.access";
    private static final String END_PERMISSION = "munecraft.end.access";
    private static final long MESSAGE_COOLDOWN = 10000L; // 10 seconds in milliseconds

    private final HashMap<UUID, Long> messageCooldowns = new HashMap<>();

    /**
     * Called when an entity enters a portal, this event will be called before the entity teleports to the target location.
     * If the event is cancelled, the entity will not teleport to the target location.
     * @param event event that is called when an entity enters a portal
     */
    @EventHandler
    public void onEntityPortalEnterEvent(EntityPortalEnterEvent event) {
        // If the event has already been cancelled by another plugin, we do nothing.
        if (event.isCancelled()) return;

        // Verify that the entity is a player before proceeding
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Material portalBlock = event.getLocation().getBlock().getType();

        // Block access based on dimension
        if (portalBlock == Material.NETHER_PORTAL && !player.hasPermission(NETHER_PERMISSION)) {
            sendCooldownMessage(player, "§cNo tienes permiso para entrar al Nether.");
            event.setCancelled(true);
        } else if (portalBlock == Material.END_PORTAL && !player.hasPermission(END_PERMISSION)) {
            sendCooldownMessage(player, "§cNo tienes permiso para entrar al End.");
            event.setCancelled(true);
        }
    }

    /**
     * Sends a cooldown message to a player if the cooldown period has passed since the last message.
     * Records the current time as the last message time for the player.
     * Dispatches an alert event with the player's name and message.
     *
     * @param player  the player to send the message to
     * @param message the message to be sent
     */
    private void sendCooldownMessage(Player player, String message) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        Long lastMessageTime = messageCooldowns.get(uuid);

        if (lastMessageTime == null || currentTime - lastMessageTime >= MESSAGE_COOLDOWN) {
            player.sendMessage(message);
            messageCooldowns.put(uuid, currentTime);
            EventDispatcher.dispatchAlert("El jugador " + player.getName() + " ha recibido el mensaje: " + message);
        }
    }
}
