package com.github.gatoartstudios.munecraft.services.discord.command;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.services.discord.embed.VerificationMinecraftEmbed;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class EmbedVerifiedMinecraft extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("embed_verified_minecraft")) return;

        // We check whether the event was triggered by a user or not, and then we verify if they have the necessary permissions to use the command.
        if (event.getMember() != null) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("No tienes permiso para usar este comando").setEphemeral(true).queue();
                EventDispatcher.dispatchAlert("El usuario" + event.getMember().getAsMention() + " no tiene permiso para usar el comando de embed_verified_minecraft");
                return;
            }
        }

        VerificationMinecraftEmbed verificationMinecraftEmbed = new VerificationMinecraftEmbed();
        MessageCreateData messageCreateData = verificationMinecraftEmbed.getEmbed();

        event.getChannel().sendMessage(messageCreateData).queue();
        event.reply("Embed de verificación enviado correctamente").setEphemeral(true).queue();
    }
}
