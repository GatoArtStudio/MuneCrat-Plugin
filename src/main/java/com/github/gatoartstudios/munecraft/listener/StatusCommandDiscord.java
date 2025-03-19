package com.github.gatoartstudios.munecraft.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class StatusCommandDiscord extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("status")) return;

        event.reply("Online").setEphemeral(true).queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        // Register slash commands
        event.getJDA().updateCommands().addCommands(
                Commands.slash("status", "Check if the bot is online")
        ).queue();
    }
}
