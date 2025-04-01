package com.github.gatoartstudios.munecraft.command;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.services.ModerationService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DevelopmentCommand implements CommandExecutor {
    private final Munecraft plugin;
    private final Map<String, BiConsumer<CommandSender, String[]>> subCommands = new HashMap<>();

    public DevelopmentCommand(Munecraft plugin) {
        this.plugin = plugin;

        // Add subcommands
        subCommands.put("get", this::getSubCommand);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

        // Check if the command sender has permission
        if (!commandSender.hasPermission("munecraft.development")) {
            commandSender.sendMessage("You don't have permission to use this command");
            LoggerCustom.warning("Player " + commandSender.getName() + " tried to use the development command without permission");
            EventDispatcher.dispatchAlert("Player " + commandSender.getName() + " tried to use the development command without permission");
            return false;
        }

        // Check if the command is a subcommand
        if (strings.length == 0 || !subCommands.containsKey(strings[0])) {
            commandSender.sendMessage("Subcommands: " + String.join(", ", subCommands.keySet()));
            return false;
        }

        // Execute the subcommand
        subCommands.get(strings[0].toLowerCase()).accept(commandSender, strings);
        return true;
    }

    private void getSubCommand(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("Usage: /development get <key>");
        }

        if (args[1].equals("online_players")) {
            sender.sendMessage("Players: " + String.join(", ", ModerationService.getOnlinePlayers()));
            Player player = (Player) sender;
            EventDispatcher.dispatchCommandExecute(player, "/development get online_players");
        }
    }
}
