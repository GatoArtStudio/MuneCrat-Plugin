package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.permission.PlayerPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorkbenchCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (!player.hasPermission(PlayerPermission.WORKBENCH.getPermission())) {
            player.sendMessage(
                    Component.text("You don't have permission to use this command")
                            .color(TextColor.color(255, 0, 0))
            );
            return false;
        }

        player.openWorkbench(player.getLocation(), true);
        player.sendMessage(Component.text("Abriendo la workbench...").color(TextColor.color(0, 255, 0)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
