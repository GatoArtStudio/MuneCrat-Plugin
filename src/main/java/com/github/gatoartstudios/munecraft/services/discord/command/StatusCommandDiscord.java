package com.github.gatoartstudios.munecraft.services.discord.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles the /status command in Discord.
 * <p>
 * When the command is called, it will reply with the string "Online".
 */
public class StatusCommandDiscord extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        // Check if the command is /status
        if (!event.getName().equals("status")) return;

        // If the command is /status, reply with "Online"
        event.reply("Online").setEphemeral(true).queue();
    }
}