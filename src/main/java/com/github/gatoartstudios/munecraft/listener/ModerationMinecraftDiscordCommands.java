package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.services.ModerationService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModerationMinecraftDiscordCommands extends ListenerAdapter {

    /*
     * Commands for moderating in the Discord server
     */

    /**
     * Handles slash command interactions. This method processes the "playerlist"
     * command to display the list of players currently online in the Minecraft server.
     *
     * @param event the event representing the slash command interaction
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        LoggerCustom.debug("Command received in ModerationMinecraftDiscordCommands: " + event.getName());

        // Display the list of players on the Minecraft server
        if (event.getName().equals("playerlist")) {
            String playerList = String.join(", ", ModerationService.getOnlinePlayers());
            event.reply("Players Online: " + playerList).setEphemeral(true).queue();
        }
    }
}