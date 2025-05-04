package com.github.gatoartstudios.munecraft.services.discord.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.awt.*;

public class VerificationMinecraftEmbed {

    public StringSelectMenu getSelectMenu() {
        return StringSelectMenu.create("select-menu-verification-minecraft")
                .addOptions(
                        SelectOption.of("Bedrock", "select-menu-verification-minecraft-bedrock")
                                .withDescription("Esta opción es para jugadores de Bedrock")
                                .withEmoji(Emoji.fromUnicode("\uD83D\uDCF1")),
                        SelectOption.of("Java", "select-menu-verification-minecraft-java")
                                .withDescription("Esta opción es para jugadores de Java")
                                .withEmoji(Emoji.fromUnicode("\uD83D\uDDA5\uFE0F"))
                )
                .setPlaceholder("Selecciona una plataforma")
                .build();
    }

    public EmbedBuilder getEmbedBuilder() {
        return new EmbedBuilder()
                .setTitle("Verification Minecraft")
                .setDescription("Para verificarte, por favor selecciona alguna de las siguientes plataformas.")
                .setColor(Color.GREEN);
    }

    public MessageCreateData getEmbed() {

        return new MessageCreateBuilder()
                .setEmbeds(getEmbedBuilder().build())
                .setComponents(ActionRow.of(getSelectMenu()))
                .build();
    }

}
