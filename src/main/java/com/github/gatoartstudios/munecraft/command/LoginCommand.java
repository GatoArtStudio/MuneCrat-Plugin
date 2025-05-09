package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLPlayerDAO;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLUserDiscordDAO;
import com.github.gatoartstudios.munecraft.helpers.PasswordUtil;
import com.github.gatoartstudios.munecraft.helpers.PlayerHelper;
import com.github.gatoartstudios.munecraft.helpers.Utils;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import com.github.gatoartstudios.munecraft.models.UserDiscordModel;
import com.github.gatoartstudios.munecraft.shared.LoginAttemptManager;
import com.github.gatoartstudios.munecraft.shared.PlayerLoginState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class LoginCommand implements CommandExecutor {
    // DAO for accessing user data in the database
    private final MySQLUserDiscordDAO userDiscordDAO = new MySQLUserDiscordDAO();
    private final MySQLPlayerDAO playerDAO = new MySQLPlayerDAO();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        // Ensure the command sender is a player
        if (!(commandSender instanceof Player player)) return false;

        // Check if the player provided a password
        if (strings.length == 0) {
            player.sendMessage(
                    Component.text("Usa: /login contrasena")
                            .color(TextColor.color(255, 0, 0)) // Red text for error
            );
            return false;
        }



        // Check if the player is blocked due to too many failed attempts
        if (LoginAttemptManager.isBlocked(player.getName())) {
            player.sendMessage(Component.text("Demasiados intentos. Espera un minuto.").color(TextColor.color(255, 0, 0)));
            return true;
        }

        // Check if the player is already logged in
        boolean isLoggedIn = PlayerLoginState.getInstance().get(player.getUniqueId());

        if (isLoggedIn) {
            player.sendMessage(
                    Component.text("Ya estas logueado en el servidor.")
                            .color(TextColor.color(255, 0, 0))
            );
        } else {
            // Retrieve user data from the database
            UserDiscordModel user = userDiscordDAO.readByMinecraftName(player.getName());

            // If the user is not registered
            if (user == null) {
                player.sendMessage(
                        Component.text("No estas registrado en el servidor o te registraste mal, recuerda que debes verificarte en el servidor de Discord, tu usuario es: ")
                                .append(Component.text(player.getName()).color(TextColor.color(255, 255, 0)))
                                .color(TextColor.color(255, 0, 0))
                );
                return true;
            }

            // Verify the provided password
            String password = strings[0];
            if (PasswordUtil.verify(password, user.getPassword())) {
                player.sendMessage(
                        Component.text("Has logueado exitosamente.")
                                .color(TextColor.color(0, 255, 0))
                );
                PlayerLoginState.getInstance().set(player.getUniqueId(), true);
                LoginAttemptManager.registerAttempt(player.getName(), true);

                // Disable safe mode
                player.setGameMode(GameMode.SURVIVAL);
                player.setInvulnerable(false);

                PlayerModel playerConfig = playerDAO.read(player.getUniqueId());

                if (playerConfig != null) {
                    playerConfig.setLoginAt(java.time.LocalDateTime.now());
                    playerDAO.update(playerConfig);

                    if (playerConfig.getLocation() != null) {
                        if (Utils.isFolia()) {
                            player.teleportAsync(PlayerHelper.deserializeLocation(playerConfig.getLocation()));
                        } else {
                            player.teleport(PlayerHelper.deserializeLocation(playerConfig.getLocation()));
                        }
                    }
                }
            } else {
                // Handle incorrect password
                player.sendMessage(
                        Component.text("Contrasena incorrecta.")
                                .color(TextColor.color(255, 0, 0))
                );
                LoginAttemptManager.registerAttempt(player.getName(), false);
            }
            return true;
        }
        return false;
    }
}
