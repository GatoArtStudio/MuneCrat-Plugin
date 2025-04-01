package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;

public class HandlesMinecraftEvents implements Listener {

    /**
     * Triggered when a player joins the server.
     * <p>
     * Sends the player's information to the event bus.
     *
     * @param event The event that contains the player's information.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EventDispatcher.dispatchPlayerJoin(event.getPlayer());
    }

    /**
     * Triggered when a player sends a chat message.
     * <p>
     * Extracts the message from the event and sends it to the event bus.
     *
     * @param event The event that contains the player's message.
     */
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Component message = event.message();

        // Extract the plain text content from the message.
        String content = PlainTextComponentSerializer.plainText().serialize(message);

        EventDispatcher.dispatchPlayerChat(event.getPlayer(), content);
    }

    /**
     * Triggered when a player leaves the server.
     * <p>
     * Sends the player's information to the event bus.
     *
     * @param event The event that contains the player's information.
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        EventDispatcher.dispatchPlayerLeave(event.getPlayer());
    }

    /**
     * Triggered when a player executes a command.
     * <p>
     * Sends the command to the event bus.
     *
     * @param event The event that contains the player's information.
     */
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        EventDispatcher.dispatchCommandExecute(event.getPlayer(), event.getMessage());
    }

    /**
     * Triggered when the server receives a command.
     * <p>
     * Logs the command and sends it to the event bus.
     *
     * @param event The event that contains the command.
     */
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand();

        EventDispatcher.dispatchLogging("Command executed: " + command);
        EventDispatcher.dispatchCommandExecute(null, command);
    }
}
