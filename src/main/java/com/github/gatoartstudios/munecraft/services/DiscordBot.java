package com.github.gatoartstudios.munecraft.services;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.config.ConfigManager;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.event.EventListener;
import com.github.gatoartstudios.munecraft.core.implement.ServiceThread;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.listener.AdminDiscordCommand;
import com.github.gatoartstudios.munecraft.listener.ModerationMinecraftDiscordCommands;
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
    private JDA jda;

    /**
     * The constructor for the Discord bot service.
     *
     * @param plugin The plugin that owns this service.
     */
    public DiscordBot(Munecraft plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();

        new EventHandler(this);
    }

    @Override
    protected void runService() throws Exception {

        // Check if the bot token is set
        if (Objects.equals(configManager.getBot().getToken(), "token_discord_bot")) {
            LoggerCustom.warning("Discord bot token is not set");
            return;
        }

        // Build the JDA
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
                .addEventListeners(
                        new OnReadyDiscord(),
                        new AdminDiscordCommand(),
                        new StatusCommandDiscord(),
                        new ModerationMinecraftDiscordCommands()
                )
                .setActivity(Activity.playing("Muñecraft"))
                .build();

        // Wait for the JDA to be ready
        jda.awaitReady();

        // Set the JDA in the service
        this.jda = jda;

        // Log a message to the console
        LoggerCustom.info("Discord bot has been started " + jda.getSelfUser().getName());
    }

    /**
     * Get the JDA.
     *
     * @return The JDA.
     */
    public JDA getJda() {
        return jda;
    }

    /**
     * An event handler for the Discord bot service.
     */
    private static class EventHandler extends EventListener {
        private DiscordBot discordBot;

        /**
         * The constructor for the event handler.
         *
         * @param discordBot The Discord bot service.
         */
        public EventHandler(DiscordBot discordBot) {
            this.discordBot = discordBot;
        }

        /**
         * This method is called when the service is loaded.
         */
        @Override
        public void onLoaded() {

            // Check if the JDA is null
            if (discordBot.getJda() == null) return;

            // Dispatch the bot ready event
            EventDispatcher.dispatchBotReady(discordBot.getJda());
        }
    }
}
