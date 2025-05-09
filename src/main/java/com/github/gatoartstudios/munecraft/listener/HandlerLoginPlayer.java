package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLPlayerDAO;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLUserDiscordDAO;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.helpers.PlayerHelper;
import com.github.gatoartstudios.munecraft.helpers.Utils;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import com.github.gatoartstudios.munecraft.models.UserDiscordModel;
import com.github.gatoartstudios.munecraft.shared.PlayerLoginState;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import java.time.LocalDateTime;


public class HandlerLoginPlayer implements Listener {
    // DAO for accessing player data
    private final MySQLPlayerDAO playerDAO = new MySQLPlayerDAO();
    // DAO for accessing Discord user data
    private final MySQLUserDiscordDAO userDiscordDAO = new MySQLUserDiscordDAO();
    private final String MESSAGE_NOT_LOGGED = "Primero debe loguearse en el servidor, usa /login contrasena.";

    /**
    * This event fires before the user logs in, or even connects to the server.
    * @param event The AsyncPlayerPreLoginEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        // Handle pre-login event
        String name = event.getName();
        LoggerCustom.debug("Player pre login: " + name);

        // Check if the player is registered
        UserDiscordModel userDiscordData = userDiscordDAO.readByMinecraftName(name);

        // If the player's data is null, it continues with the flow, but if it's not null, then it returns because the player must already be in the database.
        if (userDiscordData != null) return;

        // Disallow login if the player is not registered
        event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_FULL,
                Component.text("No estas registrado en el servidor o te registraste mal, recuerda que debes verificarte en el servidor de Discord, tu usuario es: ")
                        .append(Component.text(name).color(TextColor.color(255, 255, 0)))
                        .color(TextColor.color(255, 0, 0))
        );
    }

    /**
     * This event fires when a player logs in, or even connects to the server.
     * @param event The PlayerLoginEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        // Handle player login event
        Player player = event.getPlayer();
        LoggerCustom.debug("Player login: " + player.getName());

        PlayerModel playerConfig = playerDAO.readByMinecraftName(player.getName());

        if (playerConfig != null) {
            if (playerConfig.getUuid() == null) {
                playerConfig.setUuid(event.getPlayer().getUniqueId());
            }
            playerConfig.setActive(true);
            playerConfig.setIp(player.getAddress().getAddress().getHostAddress());
            playerDAO.update(playerConfig);
        }
    }

    /**
     * This event fires when a player joins the server.
     * @param event The PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Handle player join event
        Player player = event.getPlayer();
        LoggerCustom.debug("Player joined: " + player.getName());

        // Set the player's login state to false
        PlayerLoginState playerLoginState = PlayerLoginState.getInstance();
        playerLoginState.put(player.getUniqueId(), false);

        // Move the player to the spawn location
        if (Utils.isFolia()) {
            player.teleportAsync(player.getWorld().getSpawnLocation());
        } else {
            player.teleport(player.getWorld().getSpawnLocation());
        }

        // Enable safe mode for the player
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
    }

    /**
     * This event allows us to block attempts to write in the chat if you are not logged in yet.
     * @param event The AsyncChatEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncChatEvent event) {
        // Handle player chat event
        if (event.isCancelled()) return;

        // We get the player and check in the LoginState whether the user is logged in or not, in order to block or allow the event accordingly.
        Player player = event.getPlayer();
        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        // If the player is logged in, we return from the event; otherwise, we inform them that they are not logged in.
        if (isLoggedIn) return;

        // Cancel chat if the player is not logged in
        event.setCancelled(true);
        event.getPlayer().sendMessage(
                Component.text(MESSAGE_NOT_LOGGED)
                        .color(TextColor.color(255, 0, 0))
        );
    }

    /**
     * This event allows us to block attempts to execute commands if you are not logged in yet.
     * @param event The PlayerCommandPreprocessEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerExecuteCommand(PlayerCommandPreprocessEvent event) {
        // Handle command execution event
        if (event.isCancelled()) return;

        // We get the player and check in the LoginState whether the user is logged in or not, in order to block or allow the event accordingly.
        Player player = event.getPlayer();
        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());
        String[] parts = event.getMessage().trim().split(" ");

        if (!isLoggedIn && !parts[0].equalsIgnoreCase("/login")) {
            // Cancel command if the player is not logged in and the command is not /login
            event.setCancelled(true);
            player.sendMessage(
                    Component.text(MESSAGE_NOT_LOGGED)
                            .color(TextColor.color(255, 0, 0))
            );
        }
    }

    /**
     * This event allows us to block attempts to move if you are not logged in yet.
     * If the player tries to move but hasn't logged in yet, the event is canceled,
     * and a message is sent to them indicating that they must log in first.
     * @param event The PlayerMoveEvent
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {

        // We get the player and check in the LoginState whether the user is logged in or not, in order to block or allow the event accordingly.
        Player player = event.getPlayer();
        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        // If the player is logged in, we return from the event; otherwise, we inform them that they are not logged in.
        if (isLoggedIn) return;

        if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            event.setTo(event.getFrom());
            player.sendMessage(
                    Component.text(MESSAGE_NOT_LOGGED)
                            .color(TextColor.color(255, 0, 0))
            );
        }
    }

    /**
     * This event fires when a player leaves the server.
     * We take advantage of the event to save the player's last location before they disconnect.
     * At the same time, it will be used in the future to store the information of the last time they were online.
     * @param event The PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Handle player quit event
        PlayerLoginState.getInstance().remove(event.getPlayer().getUniqueId());

        PlayerModel playerConfig = playerDAO.read(event.getPlayer().getUniqueId());

        if (playerConfig == null) return;

        playerConfig.setLocation(PlayerHelper.serializeLocation(event.getPlayer().getLocation()));
        playerConfig.setLogoutAt(LocalDateTime.now());
        playerConfig.setActive(false);
        playerDAO.update(playerConfig);
    }

    /**
     * It is triggered when a player clicks on any inventory slot (their own or someone else's).
     * It intercepts single, double, or shift clicks.
     * These events will be blocked if the player has not logged in.
     * @param event The InventoryClickEvent
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        // We get the player and check in the LoginState whether the user is logged in or not, in order to block or allow the event accordingly.
        if (!(event.getWhoClicked() instanceof Player player)) return;

        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        // If the player is logged in, we return from the event; otherwise, we inform them that they are not logged in.
        if (isLoggedIn) return;

        event.setCancelled(true);
        player.sendMessage(
                Component.text(MESSAGE_NOT_LOGGED)
                        .color(TextColor.color(255, 0, 0))
        );
    }

    /**
     * It is triggered when a player drags a stack of items over multiple slots.
     * Canceling this event prevents the redistribution of the ItemStack.
     * This event will be blocked if the player is not logged in.
     * @param event The InventoryDragEvent
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryDragEvent(InventoryDragEvent event) {
        // We get the player and check in the LoginState whether the user is logged in or not, in order to block or allow the event accordingly.
        if (!(event.getWhoClicked() instanceof Player player)) return;

        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        // If the player is logged in, we return from the event; otherwise, we inform them that they are not logged in.
        if (isLoggedIn) return;

        event.setCancelled(true);
        player.sendMessage(
                Component.text(MESSAGE_NOT_LOGGED)
                        .color(TextColor.color(255, 0, 0))
        );
    }

    /**
     * We can detect if a player tries to drop an item from their inventory or slots.
     * If the player is not logged in, the event is canceled, thus preventing a malicious player from trying to delete another player's items.
     * @param event The PlayerDropItemEvent
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        // We get the player and check in the LoginState whether the user is logged in or not, in order to block or allow the event accordingly.
        Player player = event.getPlayer();

        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        // If the player is logged in, we return from the event; otherwise, we inform them that they are not logged in.
        if (isLoggedIn) return;

        event.setCancelled(true);
        player.sendMessage(
                Component.text(MESSAGE_NOT_LOGGED)
                        .color(TextColor.color(255, 0, 0))
        );
    }

    /**
     * If a player tries to open an inventory, this event is triggered.
     * It will check whether the player is logged in, and if not, the action will be blocked.
     * @param event The InventoryOpenEvent
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        // We get the player and check in the LoginState whether the user is logged in or not, in order to block or allow the event accordingly.
        if (!(event.getPlayer() instanceof Player player)) return;

        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        // If the player is logged in, we return from the event; otherwise, we inform them that they are not logged in.
        if (isLoggedIn) return;

        event.setCancelled(true);
        player.sendMessage(
                Component.text(MESSAGE_NOT_LOGGED)
                        .color(TextColor.color(255, 0, 0))
        );
    }
}
