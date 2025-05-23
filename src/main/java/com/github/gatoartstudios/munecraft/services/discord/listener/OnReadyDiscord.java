package com.github.gatoartstudios.munecraft.services.discord.listener;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.event.EventListener;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLGuildDiscordDAO;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.GuildDiscordModel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;

public class OnReadyDiscord extends ListenerAdapter {

    public OnReadyDiscord() {
        new Events();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LoggerCustom.info("Discord bot is ready " + event.getJDA().getSelfUser().getName());

        event.getJDA().updateCommands().addCommands(
                // Comando para ver el estado del bot, clase que maneja este comando es StatusCommandDiscord
                Commands.slash("status", "Check if the bot is online"),
                Commands.slash("embed_verified_minecraft", "Establece un embed de verificación de Minecraft"),
                // Comando para ver la configuración del bot, clase que maneja este comando es AdminDiscordCommand
                Commands.slash("admin", "Comandos de admin")
                        .addSubcommands(
                                new SubcommandData("playerlist", "Retorna la lista de jugadores en linea"),
                                new SubcommandData("config", "Retorna la configuración del bot"),
                                new SubcommandData("console", "Ejecuta un comando en la consola del servidor")
                                        .addOption(OptionType.STRING, "command", "Comando a ejecutar, no debe incluir / el comando.", true),
                                new SubcommandData("message", "Envia un mensaje al servidor de Minecraft.")
                                        .addOption(OptionType.STRING, "message", "Mensaje a enviar", true)
                        )
                        .addSubcommandGroups(
                                new SubcommandGroupData("set", "Asigna un canal de texto")
                                        .addSubcommands(
                                                new SubcommandData("log_channel", "Asigna un canal de texto para ver los logs del bot y el servidor")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("warning_channel", "Asigna un canal de texto para ver las advertencias")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("announcement_channel", "Asigna un canal de texto para enviar los anuncios")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("sanction_channel", "Asigna un canal de texto para ver las sanciones")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("report_channel", "Asigna un canal de texto para ver los reportes")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("message_channel", "Asigna un canal de texto para ver los mensajes de los jugadores")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("command_channel", "Asigna un canal de texto para historial de los comandos")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("alert_channel", "Asigna un canal de texto para ver las alertas")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("player_activity_channel", "Asigna un canal de texto para ver la actividad de los jugadores")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true),
                                                new SubcommandData("log_user_verified", "Asigna un canal de texto para ver los logs de los usuarios verificados")
                                                        .addOption(OptionType.CHANNEL, "channel", "Canal de texto", true)
                                        ),
                                new SubcommandGroupData("get", "Retorna la configuración del bot")
                                        .addSubcommands(
                                                new SubcommandData("log_channel", "Retorna el canal de logs"),
                                                new SubcommandData("warning_channel", "Retorna el canal de advertencias"),
                                                new SubcommandData("announcement_channel", "Retorna el canal de anuncios"),
                                                new SubcommandData("sanction_channel", "Retorna el canal de sanciones"),
                                                new SubcommandData("report_channel", "Retorna el canal de reportes"),
                                                new SubcommandData("message_channel", "Retorna el canal de mensajes"),
                                                new SubcommandData("command_channel", "Retorna el canal de comandos"),
                                                new SubcommandData("alert_channel", "Retorna el canal de alertas"),
                                                new SubcommandData("player_activity_channel", "Retorna el canal de actividad de jugadores"),
                                                new SubcommandData("log_user_verified", "Retorna el canal de logs de usuarios verificados")
                                        )
                        )
        ).queue();

        LoggerCustom.success("Comandos de admin cargados");

        EventDispatcher.dispatchBotReady(event.getJDA());
    }

    private static class Events extends EventListener {
        private DatabaseManager databaseManager;
        private Connection connection;
        private MySQLGuildDiscordDAO mySQLGuildDiscord;

        /**
         * Constructor of Events, this class is used to listen events of database and bot
         */
        public Events() {
            this.databaseManager = DatabaseManager.getInstance(null);
            LoggerCustom.info("Database and bot events system initialized and waiting...");

            this.mySQLGuildDiscord = new MySQLGuildDiscordDAO();
            List<GuildDiscordModel> listDiscordConfig = mySQLGuildDiscord.getAll();

            if (listDiscordConfig.isEmpty()) {
                LoggerCustom.info("No discord config found in database");
            } else {
                for (GuildDiscordModel discordConfig : listDiscordConfig) {
                    EventDispatcher.dispatchBotUpdate(discordConfig);
                }
            }
        }

        @Override
        public void onDatabaseReady(DatabaseManager databaseManager) {
            this.databaseManager = databaseManager;
            connection = databaseManager.getConnection();

            // Create a new instance of MySQLGuildDiscordDAO, is CRUD for Discord
            this.mySQLGuildDiscord = new MySQLGuildDiscordDAO();
            List<GuildDiscordModel> listConfigDiscord = mySQLGuildDiscord.getAll();

            if (listConfigDiscord.isEmpty()) {
                LoggerCustom.info("No discord config found in database");
                return;
            }

            for (GuildDiscordModel discord : listConfigDiscord) {
                EventDispatcher.dispatchBotUpdate(discord);
            }
        }

        @Override
        public void onDatabaseUpdate(DatabaseManager databaseManager) {
            this.databaseManager = databaseManager;
            connection = databaseManager.getConnection();

            // Create a new instance of MySQLGuildDiscordDAO, is CRUD for Discord
            this.mySQLGuildDiscord = new MySQLGuildDiscordDAO();
            List<GuildDiscordModel> listConfigDiscord = mySQLGuildDiscord.getAll();

            if (listConfigDiscord.isEmpty()) {
                LoggerCustom.info("No discord config found in database");
                return;
            }

            for (GuildDiscordModel discord : listConfigDiscord) {
                EventDispatcher.dispatchBotUpdate(discord);
            }
        }

        @Override
        public void onBotUpdate(GuildDiscordModel discord) {
            if (mySQLGuildDiscord == null) {
                this.mySQLGuildDiscord = new MySQLGuildDiscordDAO();
            }

            GuildDiscordModel discordConfig = mySQLGuildDiscord.read(discord.getGuildId());

            if (discordConfig != null) {
                mySQLGuildDiscord.update(discord);
            } else {
                mySQLGuildDiscord.create(discord);
            }
        }
    }
}
