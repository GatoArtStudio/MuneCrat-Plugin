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
                .setTitle("> Verificación MuñeCraft <:1c5fc47fe92d9d07a786f4d33f2904c6:1363641810613829763>")
                .setDescription("> Para acceder al servidor, debes verificarte seleccionando una de las siguientes plataformas.\n" +
                        "\n" +
                        "> Importante: Si no completas la verificación, no podrás entrar al servidor.\n" +
                        "\n" +
                        "> Sigue atentamente todos los pasos que se te indiquen durante el proceso de verificación.")
                .setColor(Color.GREEN);
    }

    public MessageCreateData getEmbed() {

        return new MessageCreateBuilder()
                .setEmbeds(getEmbedBuilder().build())
                .setComponents(ActionRow.of(getSelectMenu()))
                .build();
    }

}
