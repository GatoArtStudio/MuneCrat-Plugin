package com.github.gatoartstudios.munecraft.services.discord.helper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class ToolsEmbed {

    public static MessageEmbed createEmbed(String title, String description) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(description);

        return embed.build();
    }

    public static MessageEmbed createEmbed(String title, String description, Color color) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(description);
        embed.setColor(color);

        return embed.build();
    }

    public static MessageEmbed getEmbedVerfication() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Verification");
        embed.setDescription("To verify yourself, please click the link below.");
        embed.setColor(Color.GREEN);

        return embed.build();
    }
}
