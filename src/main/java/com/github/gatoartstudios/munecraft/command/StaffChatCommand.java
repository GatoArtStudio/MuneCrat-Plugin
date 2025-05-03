package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLPlayerDAO;
import com.github.gatoartstudios.munecraft.helpers.PlayerHelper;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StaffChatCommand implements CommandExecutor {
    private MySQLPlayerDAO playerDAO;

    public StaffChatCommand(Munecraft plugin) {
        this.playerDAO = plugin.getPlayerDAO();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        if (!(commandSender instanceof Player player)) return false;

        if (!commandSender.hasPermission("munecraft.staffchat")) {
            commandSender.sendMessage(
                    Component.text("You don't have permission to use this command")
                            .color(TextColor.color(255, 0, 0))
            );
            EventDispatcher.dispatchAlert("Player " + commandSender.getName() + " tried to use the staffchat command without permission");
            return false;
        };

        PlayerModel playerData = playerDAO.read(player.getUniqueId());

        if (playerData == null) {
            playerDAO.create(
                    new PlayerModel(
                            player.getUniqueId(),
                            player.getName(),
                            Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress(),
                            PlayerHelper.serializeInventory(player),
                            null,
                            false,
                            false,
                            null,
                            true
                    )
            );

            Component messageResponse = Component.text("Ahora estas en modo staff chat.")
                    .color(TextColor.color(0, 255, 0));

            player.sendMessage(messageResponse);

            return true;
        };

        if (playerData.getStaffChatMode()) {
            playerData.setStaffChatMode(false);
            playerDAO.update(playerData);

            Component messageResponse = Component.text("Ahora estas en modo normal.")
                    .color(TextColor.color(255, 255, 0));

            player.sendMessage(messageResponse);
        } else {
            playerData.setStaffChatMode(true);
            playerDAO.update(playerData);
            Component messageResponse = Component.text("Ahora estas en modo staff chat.")
                    .color(TextColor.color(0, 255, 0));

            player.sendMessage(messageResponse);
        }



        return false;
    }
}
