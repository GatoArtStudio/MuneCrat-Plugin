package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLPlayerDAO;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HandlesPlayerEvents implements Listener {
    private MySQLPlayerDAO playerDAO;

    public HandlesPlayerEvents(Munecraft plugin) {
        this.playerDAO = plugin.getPlayerDAO();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Handle player join event
        // Notify other players about the new player
        Component messageComponent = Component.text("El jugador ")
                .color(TextColor.color(0, 255, 0))
                .append(
                        Component.text(event.getPlayer().getName())
                                .color(TextColor.color(255, 255, 0))
                )
                .append(Component.text(" se ha unido al servidor"));

        Bukkit.broadcast(messageComponent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Handle player quit event
        // Notify other players about the player leaving
        Component messageComponent = Component.text("El jugador ")
                .color(TextColor.color(255, 90, 0))
                .append(
                        Component.text(event.getPlayer().getName())
                                .color(TextColor.color(255, 255, 0))
                )
                .append(Component.text(" se ha desconectado del servidor"));

        Bukkit.broadcast(messageComponent);
    }

    /**
     * Triggered when a player sends a chat message.
     *
     * @param event The event that contains the player's message.
     */
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {

        // If the event has already been cancelled by another plugin, we do nothing.
        if (event.isCancelled()) return;

        if (!event.getPlayer().hasPermission("munecraft.staffchat")) return;

        PlayerModel playerData = playerDAO.read(event.getPlayer().getUniqueId());

        if (playerData == null) return;

        // If the player is not in staff chat mode, we do nothing
        if (!playerData.getStaffChatMode()) return;

        // Cancel the event to prevent the player from sending the message
        event.setCancelled(true);

        // We structure the message that we will send.
        Component message = Component.text("[STAFF] ")
                .color(TextColor.color(255, 0, 0))
                .append(
                        Component.text( event.getPlayer().getName() + " ")
                                .color(TextColor.color(0, 255, 255))
                )
                .append(
                        Component.text(PlainTextComponentSerializer.plainText().serialize(event.message()))
                                .color(TextColor.color(204, 204, 204))
                );

        // We send the message only to users who have the minecraft.staffchat permission.
        Bukkit.broadcast(message, "munecraft.staffchat");
    }
}
