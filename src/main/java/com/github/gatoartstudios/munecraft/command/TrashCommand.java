package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.gui.TrashMenu;
import com.github.gatoartstudios.munecraft.permission.PlayerPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrashCommand implements CommandExecutor {
    private final TrashMenu trashMenu;

    public TrashCommand(Munecraft plugin) {
        this.trashMenu = plugin.getTrashMenu();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (!player.hasPermission(PlayerPermission.TRASH.getPermission())) {
            commandSender.sendMessage(
                    Component.text("You don't have permission to use this command")
                            .color(TextColor.color(255, 0, 0))
            );
            return false;
        }

        if (strings.length == 0) {
            trashMenu.openTrashMenu(player);
            player.sendMessage(Component.text("Abriendo el menu de basura...").color(TextColor.color(0, 255, 0)));
            return true;
        }

        return false;
    }
}
