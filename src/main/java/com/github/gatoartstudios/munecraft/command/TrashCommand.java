package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.gui.TrashMenu;
import com.github.gatoartstudios.munecraft.permission.PlayerPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrashCommand implements TabExecutor {
    private final TrashMenu trashMenu;

    public TrashCommand(Munecraft plugin) {
        this.trashMenu = plugin.getTrashMenu();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) return false;

        if (!player.hasPermission(PlayerPermission.TRASH.getPermission())) {
            commandSender.sendMessage(
                    Component.text("You don't have permission to use this command")
                            .color(TextColor.color(255, 0, 0))
            );
            return false;
        }

        if (strings.length == 0) {
            if (!player.hasPermission(PlayerPermission.TRASH_MENU.getPermission())) {
                commandSender.sendMessage(
                        Component.text("You don't have permission to use this command")
                                .color(TextColor.color(255, 0, 0))
                );
                return false;
            }
            trashMenu.openTrashMenu(player);
            player.sendMessage(Component.text("Abriendo el menu de basura...").color(TextColor.color(0, 255, 0)));
            return true;
        }

        if (strings.length == 1 && strings[0].equals("hand")) {
            if (!player.hasPermission(PlayerPermission.TRASH_HAND.getPermission())) {
                commandSender.sendMessage(
                        Component.text("You don't have permission to use this command")
                                .color(TextColor.color(255, 0, 0))
                );
                return false;
            }

            // Clear the player's hand
            ItemStack itemHand = player.getInventory().getItemInMainHand();

            if (itemHand.getType() == Material.AIR) {
                player.sendMessage(
                        Component.text("No tienes nada en la mano derecha. Debes sostener el ítem que deseas eliminar.")
                                .color(TextColor.color(255, 255, 0))
                );
                return false;
            }

            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            player.sendMessage(Component.text("Has limpiado la mano derecha, un total de " + itemHand.getAmount() + " items").color(TextColor.color(0, 255, 0)));

            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!commandSender.getName().equalsIgnoreCase("trash")) return Collections.emptyList();

        List<String> suggestions = new ArrayList<>();

        // Si aún no se escribió ningún argumento (args.length == 0 o args.length == 1 vacío),
        // ofrecemos los subcomandos disponibles.
        if (strings.length == 1) {
            if (commandSender.hasPermission(PlayerPermission.TRASH_HAND.getPermission())) {
                suggestions.add("hand");
            }


            // Filtramos según lo que el jugador ya haya empezado a escribir
            String prefix = strings[0].toLowerCase();
            suggestions.removeIf(prf -> !prf.toLowerCase().startsWith(prefix));

            Collections.sort(suggestions);
            return suggestions;
        }

        return Collections.emptyList();
    }
}
