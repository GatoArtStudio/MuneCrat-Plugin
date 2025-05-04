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
import org.bukkit.event.player.*;


public class HandlerLoginPlayer implements Listener {
    // DAO for accessing player data
    private final MySQLPlayerDAO playerDAO = new MySQLPlayerDAO();
    // DAO for accessing Discord user data
    private final MySQLUserDiscordDAO userDiscordDAO = new MySQLUserDiscordDAO();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        // Handle pre-login event
        String name = event.getName();
        LoggerCustom.debug("Player pre login: " + name);

        // Check if the player is registered
        UserDiscordModel userDiscordData = userDiscordDAO.readByMinecraftName(name);

        if (userDiscordData == null) {
            // Disallow login if the player is not registered
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_FULL,
                    Component.text("No estas registrado en el servidor o te registraste mal, recuerda que debes verificarte en el servidor de Discord, tu usuario es: ")
                            .append(Component.text(name).color(TextColor.color(255, 255, 0)))
                            .color(TextColor.color(255, 0, 0))
            );
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        // Handle player login event
        Player player = event.getPlayer();
        LoggerCustom.debug("Player login: " + player.getName());

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Handle player join event
        Player player = event.getPlayer();
        LoggerCustom.debug("Player joined: " + player.getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncChatEvent event) {
        // Handle player chat event
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        if (!isLoggedIn) {
            // Cancel chat if the player is not logged in
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    Component.text("Primero debe loguearse en el servidor, usa /login contrasena.")
                            .color(TextColor.color(255, 0, 0))
            );
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerExecuteCommand(PlayerCommandPreprocessEvent event) {
        // Handle command execution event
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());
        String[] parts = event.getMessage().trim().split(" ");

        if (!isLoggedIn && !parts[0].equalsIgnoreCase("/login")) {
            // Cancel command if the player is not logged in and the command is not /login
            event.setCancelled(true);
            player.sendMessage(
                    Component.text("Primero debe loguearse en el servidor, usa /login contrasena.")
                            .color(TextColor.color(255, 0, 0))
            );
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());
        if (!isLoggedIn) {

            if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
                event.setTo(event.getFrom());
                player.sendMessage(
                        Component.text("Primero debe loguearse en el servidor, usa /login contrasena.")
                                .color(TextColor.color(255, 0, 0))
                );
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Handle player quit event
        PlayerLoginState.getInstance().remove(event.getPlayer().getUniqueId());

        PlayerModel playerConfig = playerDAO.read(event.getPlayer().getUniqueId());

        if (playerConfig != null) {
            playerConfig.setUltimateLocation(PlayerHelper.serializeLocation(event.getPlayer().getLocation()));
            playerDAO.update(playerConfig);
        }
    }
}
