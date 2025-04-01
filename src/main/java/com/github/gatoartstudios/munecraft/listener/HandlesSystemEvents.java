package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.event.EventListener;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.helpers.Utils;
import com.github.gatoartstudios.munecraft.models.Discord;
import com.github.gatoartstudios.munecraft.models.DiscordChannels;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HandlesSystemEvents extends EventListener {
    private JDA discordBot;
    private Munecraft plugin;

    // The Long key corresponds to the guildId, and the DiscordChannels an object with TextChannel
    private final Map<Long, DiscordChannels> channels = new HashMap<>();

    /**
     * Constructs a HandlesSystemEvents instance with the specified plugin.
     *
     * @param plugin The Munecraft plugin instance.
     */
    public HandlesSystemEvents(Munecraft plugin) {
        this.plugin = plugin;
    }

    /**
     * Invoked when the bot is ready.
     * <p>
     * Sets the Discord bot instance and logs information about the bot.
     *
     * @param discordBot The JDA instance representing the Discord bot.
     */
    @Override
    public void onBotReady(JDA discordBot) {
        this.discordBot = discordBot;
        LoggerCustom.info("Receiving data from Discord..." + discordBot.getSelfUser().getName());
    }

    /**
     * Called when the bot is updated with new data from the database.
     * <p>
     * This method is responsible for setting up the Discord channels for the bot based on the data received.
     * <p>
     * The channels are stored in a map with the guildId as the key and the DiscordChannels object as the value.
     *
     * @param discord The updated Discord object with the new data.
     */

    @Override
    public void onBotUpdate(Discord discord) {
        long guildId = discord.getGuildId();

        LoggerCustom.info("Receiving data from Discord..." + guildId);

        if (discordBot == null) {
            LoggerCustom.warning("The bot is not initialized to set up the channels in HandlesSystemEvents");
            return;
        }

        // Create a DiscordChannels object to store the channels for this guild
        DiscordChannels discordChannels = new DiscordChannels();

        if (discord.getLogChannelId() != 0) {
            discordChannels.setLogChannel(discordBot.getTextChannelById(discord.getLogChannelId()));
        }
        if (discord.getWarningChannelId() != 0) {
            discordChannels.setWarningChannel(discordBot.getTextChannelById(discord.getWarningChannelId()));
        }
        if (discord.getAnnouncementChannelId() != 0) {
            discordChannels.setAnnouncementChannel(discordBot.getTextChannelById(discord.getAnnouncementChannelId()));
        }
        if (discord.getSanctionChannelId() != 0) {
            discordChannels.setSanctionChannel(discordBot.getTextChannelById(discord.getSanctionChannelId()));
        }
        if (discord.getReportChannelId() != 0) {
            discordChannels.setReportChannel(discordBot.getTextChannelById(discord.getReportChannelId()));
        }
        if (discord.getMessageChannelId() != 0) {
            discordChannels.setMessageChannel(discordBot.getTextChannelById(discord.getMessageChannelId()));
        }
        if (discord.getCommandChannelId() != 0) {
            discordChannels.setCommandChannel(discordBot.getTextChannelById(discord.getCommandChannelId()));
        }
        if (discord.getAlertChannelId() != 0) {
            discordChannels.setAlertChannel(discordBot.getTextChannelById(discord.getAlertChannelId()));
        }
        if (discord.getPlayerActivityChannelId() != 0) {
            discordChannels.setPlayerActivityChannel(discordBot.getTextChannelById(discord.getPlayerActivityChannelId()));
        }

        // Store the channels in the map
        channels.put(guildId, discordChannels);

        LoggerCustom.info("Channels stored correctly for Guild ID: " + guildId);
    }

    /**
     * Called when a player executes a command.
     * <p>
     * This method sends a message to the command channel and the player activity channel with information about the player who executed the command.
     * <p>
     * The information sent includes the command executed, the sender (player or console), UUID of the player, and the world where the command was executed.
     *
     * @param player  The player who executed the command.
     * @param command The command executed.
     */
    @Override
    public void onCommandExecute(Player player, String command) {
        if (discordBot == null && channels.isEmpty()) return;

        // Send message to command channel
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Command executed")
                .setColor(Color.GREEN)
                .addField("Command", command, false)
                .addField("Sender", player != null ? player.getName() : "Console", false)
                .addField("UUID", player != null ? player.getUniqueId().toString() : "Console", false)
                .addField("World", player != null ? player.getWorld().getName() : "Console", false);

        MessageEmbed messageEmbed = embed.build();

        for (DiscordChannels discordChannels : channels.values()) {
            if (discordChannels.getCommandChannel() != null) {
                discordChannels.getCommandChannel().sendMessageEmbeds(messageEmbed).queue();
            }

            if (discordChannels.getPlayerActivityChannel() != null && !discordChannels.getPlayerActivityChannel().equals(discordChannels.getCommandChannel())) {
                discordChannels.getPlayerActivityChannel().sendMessageEmbeds(messageEmbed).queue();
            }
        }
    }

    /**
     * Sends a logging message to the log channel.
     *
     * This method is triggered whenever a logging event occurs and sends the message
     * to the designated log channel in Discord.
     *
     * @param message The logging message to be sent.
     */
    @Override
    public void onLogging(String message) {
        if (discordBot == null && channels.isEmpty()) return;

        // Send message to logging channel
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Logging")
                .setColor(Color.CYAN)
                .addField("Message", message, false);

        MessageEmbed messageEmbed = embed.build();

        for (DiscordChannels discordChannels : channels.values()) {
            if (discordChannels.getLogChannel() != null) {
                discordChannels.getLogChannel().sendMessageEmbeds(messageEmbed).queue();
            }
        }
    }

    /**
     * Called when a player joins the server.
     * <p>
     * This method sends a message to the player activity channel with information about the player who joined.
     * <p>
     * The information sent includes the player's name, UUID, IP address, location, and world.
     *
     * @param player The player who joined.
     */
    @Override
    public void onPlayerJoin(Player player) {
        if (discordBot == null && channels.isEmpty()) return;

        // Send message to player activity channel
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Player joined")
                .setColor(Color.MAGENTA)
                .addField("Player", player.getName(), false)
                .addField("UUID", player.getUniqueId().toString(), false)
                .addField("IP", "||" + Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress() + "||", false)
                .addField("Location", "X: " + player.getLocation().getX() + " Y: " + player.getLocation().getY() + " Z: " + player.getLocation().getZ(), false)
                .addField("World", player.getWorld().getName(), false);

        MessageEmbed messageEmbed = embed.build();

        for (DiscordChannels discordChannels : channels.values()) {
            if (discordChannels.getPlayerActivityChannel() != null) {
                discordChannels.getPlayerActivityChannel().sendMessageEmbeds(messageEmbed).queue();
            }
        }
    }

    /**
     * Called when a player sends a chat message.
     * <p>
     * This method sends a message to the player activity channel with information about the player who sent the message.
     * <p>
     * The information sent includes the player's name, UUID, and message.
     *
     * @param player The player who sent the message.
     * @param message The message sent by the player.
     */
    @Override
    public void onPlayerChat(Player player, String message) {
        if (discordBot == null && channels.isEmpty()) return;

        // Send message to logging channel
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Player chat")
                .setColor(new Color(120, 255, 169))
                .addField("Player", player.getName(), false)
                .addField("UUID", player.getUniqueId().toString(), false)
                .addField("Message", message, false);

        MessageEmbed messageEmbed = embed.build();

        for (DiscordChannels discordChannels : channels.values()) {
            if (discordChannels.getPlayerActivityChannel() != null) {
                discordChannels.getPlayerActivityChannel().sendMessageEmbeds(messageEmbed).queue();
            }
        }
    }

    /**
     * Called when a player leaves the server.
     * <p>
     * This method sends a message to the player activity channel with information about the player who left.
     * <p>
     * The information sent includes the player's name, UUID, IP address, location, and world.
     *
     * @param player The player who left.
     */
    @Override
    public void onPlayerLeave(Player player) {
        if (discordBot == null && channels.isEmpty()) return;

        // Send message to logging channel
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Player left")
                .setColor(Color.RED)
                .addField("Player", player.getName(), false)
                .addField("UUID", player.getUniqueId().toString(), false)
                .addField("IP", "||" + Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress() + "||", false)
                .addField("Location","X: " + player.getLocation().getX() + " Y: " + player.getLocation().getY() + " Z: " + player.getLocation().getZ() , false)
                .addField("World", player.getWorld().getName(), false);

        MessageEmbed messageEmbed = embed.build();

        for (DiscordChannels discordChannels : channels.values()) {
            if (discordChannels.getPlayerActivityChannel() != null) {
                discordChannels.getPlayerActivityChannel().sendMessageEmbeds(messageEmbed).queue();
            }
        }
    }

    /**
     * Executes a server command.
     * <p>
     * This method is called when a command is sent to the server and is executed in the main thread.
     * <p>
     * If the server is running Folia, the command is executed asynchronously in a separate thread.
     * Otherwise, the command is executed synchronously in the main thread.
     *
     * @param commandToExecute The command to be executed.
     */
    @Override
    public void onExecuteServerCommand(String commandToExecute) {
        boolean isFolia = Utils.isFolia();

        if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> {

                Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
                    boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);

                    if (result) {
                        EventDispatcher.dispatchLogging("Command executed: " + commandToExecute);
                        EventDispatcher.dispatchExecuteServerCommandResult(true, "Command executed: " + commandToExecute);
                    } else {
                        EventDispatcher.dispatchLogging("Command not executed: " + commandToExecute);
                        EventDispatcher.dispatchExecuteServerCommandResult(false, "Command not executed: " + commandToExecute);
                    }
                });
            });
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute);

                if (result) {
                    EventDispatcher.dispatchLogging("Command executed: " + commandToExecute);
                    EventDispatcher.dispatchExecuteServerCommandResult(true, "Command executed: " + commandToExecute);
                } else {
                    EventDispatcher.dispatchLogging("Command not executed: " + commandToExecute);
                    EventDispatcher.dispatchExecuteServerCommandResult(false, "Command not executed: " + commandToExecute);
                }
            });
        }
    }

    /**
     * Sends a message to all alert channels with the given message.
     * <p>
     * This method is called when an alert is triggered and sends a message to all alert channels.
     * <p>
     * The message is sent with the title "Alert" and the color red.
     *
     * @param message The message to be sent to all alert channels.
     */
    @Override
    public void onAlert(String message) {
        if (discordBot == null && channels.isEmpty()) return;

        // Send message to alert channel
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Alert")
                .setColor(Color.RED)
                .addField("Message", message, false);

        MessageEmbed messageEmbed = embed.build();

        for (DiscordChannels discordChannels : channels.values()) {
            if (discordChannels.getAlertChannel() != null) {
                discordChannels.getAlertChannel().sendMessageEmbeds(messageEmbed).queue();
            }
        }
    }
}