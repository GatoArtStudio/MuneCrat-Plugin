package com.github.gatoartstudios.munecraft.services.discord.listener;

import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class LogCommandExecuted extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        LoggerCustom.debug("Command received from Discord: " + event.getName() + " by " + event.getUser().getName());
    }
}
