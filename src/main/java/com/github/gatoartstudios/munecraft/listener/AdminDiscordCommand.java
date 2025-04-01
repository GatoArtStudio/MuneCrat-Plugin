package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.core.enums.EventType;
import com.github.gatoartstudios.munecraft.core.event.Event;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.event.EventListener;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.Discord;
import com.github.gatoartstudios.munecraft.models.DiscordChannels;
import com.github.gatoartstudios.munecraft.services.ModerationService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class AdminDiscordCommand extends ListenerAdapter {
    private static DatabaseManager databaseManager;
    private static Map<Long, Discord> discordConfigs = new HashMap<>();

    public AdminDiscordCommand() {
        new Events();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        LoggerCustom.debug("Comando recibido en AdminDiscordCommand: " + event.getName());

        if (!event.getName().equals("admin")) return;

        if (event.getMember() != null) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("No tienes permiso para usar este comando").setEphemeral(true).queue();
                EventDispatcher.dispatchAlert("El usuario" + event.getMember().getAsMention() + " no tiene permiso para usar el comando de admin");
                return;
            }
        }

        String subcommandName = event.getSubcommandName();
        String subcommandGroup = event.getSubcommandGroup();

        if (subcommandGroup != null) {
            switch (event.getSubcommandGroup()) {
                case "set":
                    handleSetCommand(event);
                    break;
                case "get":
                    handleGetCommand(event);
                    break;
                case null, default:
                    LoggerCustom.warning("SubcommandGroup no reconocido: " + event.getSubcommandGroup());
                    break;
            }
            return;
        }

        if (subcommandName != null) {
            switch (event.getSubcommandName()) {
                case "playerlist":
                    List<String> onlinePlayers = ModerationService.getOnlinePlayers();
                    if (onlinePlayers.isEmpty()) {
                        event.reply("No hay jugadores en linea").setEphemeral(true).queue();
                        break;
                    }

                    event.reply("Jugadores en Linea: " + String.join(", ", onlinePlayers)).setEphemeral(true).queue();
                    break;
                case "console":

                    // Enviamos respuesta inmediata para evitar el timeout
                    event.reply("Ejecutando comando...").setEphemeral(true).queue(interactionHook -> {
                        // Ejecutamos el evento para ejecutar el comando
                        EventDispatcher.dispatchExecuteServerCommand(event.getOption("command").getAsString());

                        // Escuchamos el evento para obtener el resultado
                        Event eventManager = Event.getInstance();

                        Consumer<Object[]> listener = new Consumer<Object[]>() {
                            @Override
                            public void accept(Object[] objects) {
                                Boolean status = (Boolean) objects[0];
                                String message = (String) objects[1];
                                String s = status ? "Exitoso" : "Fallido";

                                // Editamos el mensaje inicial en lugar de hacer un nuevo replay
                                interactionHook.editOriginal("Comando ejecutado " + s).queue();

                                // Remove listener
                                eventManager.removeListener(EventType.EXECUTE_SERVER_COMMAND_RESULT, this);
                            }
                        };

                        eventManager.addListener(EventType.EXECUTE_SERVER_COMMAND_RESULT, listener);
                    });
                    break;
                case "config":
                    Discord discordConfig = discordConfigs.get(Objects.requireNonNull(event.getGuild()).getIdLong());

                    if (discordConfig == null) {
                        event.reply("No se encontraron configuraciones para este servidor").setEphemeral(true).queue();
                        break;
                    }

                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Configuración del bot")
                            .setColor(Color.CYAN)
                            .addField("Log Channel", "<#" + discordConfig.getLogChannelId() + ">", false)
                            .addField("Warning Channel", "<#" + discordConfig.getWarningChannelId() + ">", false)
                            .addField("Announcement Channel", "<#" + discordConfig.getAnnouncementChannelId() + ">", false)
                            .addField("Sanction Channel", "<#" + discordConfig.getSanctionChannelId() + ">", false)
                            .addField("Report Channel", "<#" + discordConfig.getReportChannelId() + ">", false)
                            .addField("Message Channel", "<#" + discordConfig.getMessageChannelId() + ">", false)
                            .addField("Command Channel", "<#" + discordConfig.getCommandChannelId() + ">", false)
                            .addField("Alert Channel", "<#" + discordConfig.getAlertChannelId() + ">", false)
                            .addField("Player Activity Channel", "<#" + discordConfig.getPlayerActivityChannelId() + ">", false);

                    event.replyEmbeds(embed.build()).queue();
                    break;
                case null, default:
                    LoggerCustom.warning("SubcommandName no reconocido: " + event.getSubcommandName());
                    break;
            }
            return;
        }

        event.reply("Comando no reconocido").setEphemeral(true).queue();
    }

    private static class Events extends EventListener {
        @Override
        public void onDatabaseReady(DatabaseManager db) {
            databaseManager = db;
        }

        @Override
        public void onBotUpdate(Discord discord) {
            discordConfigs.put(discord.getGuildId(), discord);
        }
    }

    private void handleSetCommand(SlashCommandInteractionEvent event) {
        String subcommand = event.getSubcommandName();
        TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
        Discord discordConfig = discordConfigs.get(Objects.requireNonNull(event.getGuild()).getIdLong());

        if (discordConfig == null) {
            discordConfig = new Discord();
            discordConfig.setGuildId(event.getGuild().getIdLong());
        }

        switch (subcommand) {
            case "log_channel":
                // Aquí guardas el canal en la configuración de tu plugin
                discordConfig.setLogChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de logs establecido en: " + channel.getAsMention()).queue();
                break;
            case "warning_channel":
                discordConfig.setWarningChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de advertencias establecido en: " + channel.getAsMention()).queue();
                break;
            case "announcement_channel":
                discordConfig.setAnnouncementChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de anuncios establecido en: " + channel.getAsMention()).queue();
                break;
            case "sanction_channel":
                discordConfig.setSanctionChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de sanciones establecido en: " + channel.getAsMention()).queue();
                break;
            case "report_channel":
                discordConfig.setReportChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de reportes establecido en: " + channel.getAsMention()).queue();
                break;
            case "message_channel":
                discordConfig.setMessageChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de mensajes establecido en: " + channel.getAsMention()).queue();
                break;
            case "command_channel":
                discordConfig.setCommandChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de comandos establecido en: " + channel.getAsMention()).queue();
                break;
            case "alert_channel":
                discordConfig.setAlertChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de alertas establecido en: " + channel.getAsMention()).queue();
                break;
            case "player_activity_channel":
                discordConfig.setPlayerActivityChannelId(Long.parseLong(channel.getId()));
                EventDispatcher.dispatchBotUpdate(discordConfig);
                event.reply("Canal de actividad de jugadores establecido en: " + channel.getAsMention()).queue();
                break;
            default:
                event.reply("Subcomando desconocido.").setEphemeral(true).queue();
        }
    }

    private void handleGetCommand(SlashCommandInteractionEvent event) {
        String subcommand = event.getSubcommandName();
        Discord discordConfig = discordConfigs.get(Objects.requireNonNull(event.getGuild()).getIdLong());

        if (discordConfig == null) {
            event.reply("No se encontraron configuraciones para este servidor").setEphemeral(true).queue();
            return;
        }

        switch (subcommand) {
            case "log_channel":
                event.reply("Canal de logs: <#" + discordConfig.getLogChannelId() + ">").setEphemeral(true).queue();
                break;
            case "warning_channel":
                event.reply("Canal de advertencias: <#" + discordConfig.getWarningChannelId() + ">").setEphemeral(true).queue();
                break;
            case "announcement_channel":
                event.reply("Canal de anuncios: <#" + discordConfig.getAnnouncementChannelId() + ">").setEphemeral(true).queue();
                break;
            case "sanction_channel":
                event.reply("Canal de sanciones: <#" + discordConfig.getSanctionChannelId() + ">").setEphemeral(true).queue();
                break;
            case "report_channel":
                event.reply("Canal de reportes: <#" + discordConfig.getReportChannelId() + ">").setEphemeral(true).queue();
                break;
            case "message_channel":
                event.reply("Canal de mensajes: <#" + discordConfig.getMessageChannelId() + ">").setEphemeral(true).queue();
                break;
            case "command_channel":
                event.reply("Canal de comandos: <#" + discordConfig.getCommandChannelId() + ">").setEphemeral(true).queue();
                break;
            case "alert_channel":
                event.reply("Canal de alertas: <#" + discordConfig.getAlertChannelId() + ">").setEphemeral(true).queue();
                break;
            case "player_activity_channel":
                event.reply("Canal de actividad de jugadores: <#" + discordConfig.getPlayerActivityChannelId() + ">").setEphemeral(true).queue();
                break;
            default:
                event.reply("Subcomando desconocido.").setEphemeral(true).queue();
        }
    }
}
