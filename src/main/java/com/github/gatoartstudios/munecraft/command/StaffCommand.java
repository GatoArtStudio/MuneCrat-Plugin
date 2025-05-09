package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLPlayerDAO;
import com.github.gatoartstudios.munecraft.helpers.PlayerHelper;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StaffCommand implements CommandExecutor {
    private final MySQLPlayerDAO mySQLPlayerDAO;

    public StaffCommand(Munecraft plugin) {
        this.mySQLPlayerDAO = plugin.getPlayerDAO();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        // Check if the command sender has permission
        if (!commandSender.hasPermission("munecraft.staff")) {
            commandSender.sendMessage(
                    Component.text("You don't have permission to use this command")
                            .color(TextColor.color(255, 0, 0))
            );
            EventDispatcher.dispatchAlert("Player " + commandSender.getName() + " tried to use the development command without permission");
            return false;
        }

        // Check if the command sender is a player
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return false;
        }

        // Search for the player in the database
        PlayerModel playerModel = mySQLPlayerDAO.read(player.getUniqueId());

        // If the player is not found, create a new entry in the database
        if (playerModel == null) {

            commandSender.sendMessage(
                    Component.text("Ahora tendras tu inventario de staff, el inventario normal sera guardado.")
                            .color(TextColor.color(0, 255, 228 ))
            );

            // Create a new PlayerData object
            PlayerModel playerData = new PlayerModel(
                    player.getUniqueId(),
                    player.getName(),
                    Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(),
                    PlayerHelper.serializeInventory(player)
            );

            playerData.setModeStaff(true);

            mySQLPlayerDAO.create(playerData);

            player.getInventory().clear();

            if (player.hasPermission("munecraft.staff.op")) {
                player.setOp(true);
            }

            commandSender.sendMessage(
                    Component.text("Ahora has activado el modo staff, ahora tendras tu inventario de staff.")
                            .color(TextColor.color(0, 255, 0))
            );

            return true;
        }

        // If the player has permission, toggle their staff mode
        if (playerModel.isModeStaff()) {
            // This block is to disable staff mode
            playerModel.setModeStaff(false);
            if (player.isOp()) {
                player.setOp(false);
            }

            // We save the player's staff inventory
            playerModel.setInventoryStaff(PlayerHelper.serializeInventory(player));

            // Clear the player's inventory
            player.getInventory().clear();

            // We set up the inventory for non-staff mode
            if (playerModel.getInventory() != null) {
                PlayerHelper.deserializeInventory(player, playerModel.getInventory());
            }

            if (player.getGameMode() != GameMode.SURVIVAL) {
                player.setGameMode(GameMode.SURVIVAL);
            }

            commandSender.sendMessage(
                    Component.text("Ahora has desactivado el modo staff, ahora tendras tu inventario normal.")
                            .color(TextColor.color(255, 255, 0))
            );


            mySQLPlayerDAO.update(playerModel);

            return true;

        } else {
            // This block is to enable staff mode
            playerModel.setModeStaff(true);
            if (!player.isOp() && player.hasPermission("munecraft.staff.op")) {
                player.setOp(true);
            }

            // We save the player's normal inventory
            playerModel.setInventory(PlayerHelper.serializeInventory(player));

            // Clear the player's inventory
            player.getInventory().clear();

            // We set up the inventory for staff mode
            if (playerModel.getInventoryStaff() != null) {
                PlayerHelper.deserializeInventory(player, playerModel.getInventoryStaff());
            }

            commandSender.sendMessage(
                    Component.text("Ahora has activado el modo staff, ahora tendras tu inventario de staff.")
                            .color(TextColor.color(0, 255, 0))
            );

            mySQLPlayerDAO.update(playerModel);

            return true;
        }
    }
}
