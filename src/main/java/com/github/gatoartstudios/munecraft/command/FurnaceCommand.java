package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.gui.FurnaceMenu;
import com.github.gatoartstudios.munecraft.permission.OperatorPermission;
import com.github.gatoartstudios.munecraft.permission.PlayerPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FurnaceCommand implements TabExecutor {
    private final FurnaceMenu furnaceMenu;

    public FurnaceCommand(Munecraft plugin) {
        this.furnaceMenu = plugin.getFurnaceMenu();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return false;

        // ----------------------
        //   SUBCOMMAND "set"
        // ----------------------
        if (strings.length == 4 && strings[0].equals("set")) {
            if (!player.hasPermission(OperatorPermission.STAFF_OP.getPermission())) {
                commandSender.sendMessage(
                        Component.text("You don't have permission to use this command")
                                .color(TextColor.color(255, 0, 0))
                );
                return false;
            }

            World world = player.getWorld();
            int x, y, z;
            try {
                x = Integer.parseInt(strings[1]);
                y = Integer.parseInt(strings[2]);
                z = Integer.parseInt(strings[3]);
            } catch (NumberFormatException e) {
                player.sendMessage(
                        Component.text("Uso: /furnace set <x> <y> <z>")
                                .color(TextColor.color(255, 0, 0))
                );
                return false;
            }
            Location location = new Location(world, x, y, z);
            furnaceMenu.setLocationOriginWorld(location);

            Component clickTp = Component.text("Ir a la posicion de origen")
                    .color(TextColor.color(255, 255, 0))
                    .clickEvent(ClickEvent.runCommand(
                            "/tp " + player.getName() + " " +
                            location.getBlockX() + " " +
                            location.getBlockY() + " " +
                            location.getBlockZ()
                    ));

            player.sendMessage(
                    Component.text("Posicion de origen establecida en: " +
                                    location.getBlockX() + ", " +
                                    location.getBlockY() + ", " +
                                    location.getBlockZ())
                            .color(TextColor.color(0, 255, 0))
                            .append(clickTp)
            );
            return true;
        }

        // ----------------------
        //   MESSAGE USAGE
        // ----------------------
        if (strings.length >= 1) {
            player.sendMessage(
                    Component.text("Uso: /furnace")
                            .color(TextColor.color(255, 0, 0))
            );
            return false;
        }

        // ----------------------
        //   DEFAULT COMMAND
        // ----------------------
        if (!player.hasPermission(PlayerPermission.FURNACE.getPermission())) {
            commandSender.sendMessage(
                    Component.text("You don't have permission to use this command")
                            .color(TextColor.color(255, 0, 0))
            );
            return false;
        }

        if (furnaceMenu.openFurnaceMenu(player)) {
            player.sendMessage(
                    Component.text("Horno creado")
                        .color(TextColor.color(0, 255, 0))
            );
            return true;
        } else {
            player.sendMessage(
                    Component.text("No se pudo crear el horno.")
                            .color(TextColor.color(255, 0, 0))
            );
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return Collections.emptyList();

        if (!player.hasPermission(OperatorPermission.STAFF_OP.getPermission())) return Collections.emptyList();

        if (strings.length == 1) {
            return List.of("set");
        }

        if (strings.length >= 2 && "set".equalsIgnoreCase(strings[0])) {
            Location loc = player.getLocation();
            return switch (strings.length) {
                case 2 ->
                        List.of(String.valueOf(loc.getBlockX()));
                case 3 ->
                        List.of(String.valueOf(loc.getBlockY()));
                case 4 ->
                        List.of(String.valueOf(loc.getBlockZ()));
                default -> Collections.emptyList();
            };
        }
        return Collections.emptyList();
    }
}
