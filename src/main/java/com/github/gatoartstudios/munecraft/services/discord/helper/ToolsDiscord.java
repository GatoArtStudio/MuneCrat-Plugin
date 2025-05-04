package com.github.gatoartstudios.munecraft.services.discord.helper;

import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLGuildDiscordDAO;
import com.github.gatoartstudios.munecraft.models.GuildDiscordModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.Objects;

public class ToolsDiscord {
    public static JDA botDiscord = null;
    private static MySQLGuildDiscordDAO guildDiscordDAO = new MySQLGuildDiscordDAO();

    public static boolean addRoleUser(long idUser, long idRole, long guildId, String minecraftName) {
        if (botDiscord == null) return false;

        Guild guild = botDiscord.getGuildById(guildId);

        if (guild == null) return false;

        Member member = guild.getMemberById(idUser);
        Role role = guild.getRoleById(idRole);

        if (member != null && role != null) {
            guild.addRoleToMember(member, role).queue();

            GuildDiscordModel guildDiscord = guildDiscordDAO.read(guildId);
            if (guildDiscord == null) return true;

            if (guildDiscord.getLogUserVerifiedId() == null) return true;

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Usuario verificado")
                    .setColor(Color.GREEN)
                    .addField("Usuario", member.getUser().getName(), true)
                    .addField("ID", member.getUser().getId(), true)
                    .addField("Mention", member.getUser().getAsMention(), true)
                    .addField("Minecraft", minecraftName, true);

            MessageEmbed messageEmbed = embed.build();

            TextChannel textChannel = botDiscord.getTextChannelById(guildDiscord.getLogUserVerifiedId());
            if (textChannel == null) return true;

            textChannel.sendMessageEmbeds(messageEmbed).queue();

            return true;
        }

        return false;
    }
}
