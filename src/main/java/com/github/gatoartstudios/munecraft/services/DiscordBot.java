package com.github.gatoartstudios.munecraft.services;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.config.ConfigManager;
import com.github.gatoartstudios.munecraft.core.implement.ServiceThread;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.listener.OnReadyDiscord;
import com.github.gatoartstudios.munecraft.listener.StatusCommandDiscord;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.EnumSet;
import java.util.Objects;

public class DiscordBot extends ServiceThread {
    private Munecraft plugin;
    private ConfigManager configManager;

    public DiscordBot(Munecraft plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    protected void runService() throws Exception {

        if (Objects.equals(configManager.getBot().getToken(), "token_discord_bot")) {
            LoggerCustom.warning("Discord bot token is not set");
            return;
        }

        JDA jda = JDABuilder.createDefault(
                        configManager.getBot().getToken(),
                        EnumSet.of(
                                GatewayIntent.DIRECT_MESSAGES,
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.MESSAGE_CONTENT
                        )
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new OnReadyDiscord())
                .addEventListeners(new StatusCommandDiscord())
                .setActivity(Activity.playing("Muñecraft"))
                .build();

        jda.awaitReady();

        LoggerCustom.info("Discord bot has been started " + jda.getSelfUser().getName());
    }
}
