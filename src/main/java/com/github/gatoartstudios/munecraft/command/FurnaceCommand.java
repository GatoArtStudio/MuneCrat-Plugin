package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.permission.PlayerPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FurnaceCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (!player.hasPermission(PlayerPermission.FURNACE.getPermission())) {
            commandSender.sendMessage(
                    Component.text("You don't have permission to use this command")
                            .color(TextColor.color(255, 0, 0))
            );
            return false;
        }

        World world = player.getWorld();
        Location locationFurnace = new Location(world, player.getLocation().getX(), player.getLocation().getY() + 5, player.getLocation().getZ());
        Block blockInWorld = world.getBlockAt(locationFurnace);

        if (blockInWorld.getType() != Material.FURNACE && blockInWorld.getType() == Material.AIR) {
            blockInWorld.setType(Material.FURNACE);
            player.sendMessage(Component.text("Horno creado").color(TextColor.color(0, 255, 0)));
        } else {
            player.sendMessage(Component.text("No se pudo crear el horno.").color(TextColor.color(255, 0, 0)));
            return false;
        }

        Furnace furnace = (Furnace) blockInWorld.getState();
        Inventory furnaceInventory = furnace.getInventory();
        player.openInventory(furnaceInventory);

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
