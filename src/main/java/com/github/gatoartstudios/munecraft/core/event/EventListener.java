package com.github.gatoartstudios.munecraft.core.event;

import com.github.gatoartstudios.munecraft.core.enums.EventType;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.GuildDiscordModel;
import net.dv8tion.jda.api.JDA;
import org.bukkit.entity.Player;

public abstract class EventListener {

    public EventListener() {
        LoggerCustom.success("Event listener has been initialized");
        /*
         * Command execute
         */
        Event eventManager = Event.getInstance();
        eventManager.addListener(EventType.COMMAND_EXECUTE, args -> {
            Player player = (Player) args[0];
            String commandExecuted = (String) args[1];
            onCommandExecute(player, commandExecuted);
        });
        eventManager.addListener(EventType.ALERT, args -> {
            String message = (String) args[0];
            onAlert(message);
        });
        eventManager.addListener(EventType.BOT_READY, args -> {
            JDA discordBot = (JDA) args[0];
            onBotReady(discordBot);
        });
        eventManager.addListener(EventType.BOT_UPDATE, args -> {
            GuildDiscordModel guildDiscordModel = (GuildDiscordModel) args[0];
            onBotUpdate(guildDiscordModel);
        });
        eventManager.addListener(EventType.PLAYER_JOIN, args -> {
            Player player = (Player) args[0];
            onPlayerJoin(player);
        });
        eventManager.addListener(EventType.PLAYER_CHAT, args -> {
            Player player = (Player) args[0];
            String message = (String) args[1];
            onPlayerChat(player, message);
        });
        eventManager.addListener(EventType.PLAYER_LEAVE, args -> {
            Player player = (Player) args[0];
            onPlayerLeave(player);
        });
        eventManager.addListener(EventType.LOGGING, args -> {
            String message = (String) args[0];
            onLogging(message);
        });
        eventManager.addListener(EventType.LOGGING_WARNING, args -> {
            String message = (String) args[0];
            onLoggingWarning(message);
        });
        eventManager.addListener(EventType.LOGGING_ERROR, args -> {
            String message = (String) args[0];
            onLoggingError(message);
        });
        eventManager.addListener(EventType.LOGGING_DEBUG, args -> {
            String message = (String) args[0];
            onLoggingDebug(message);
        });
        eventManager.addListener(EventType.LOGGING_SUCCESS, args -> {
            String message = (String) args[0];
            onLoggingSuccess(message);
        });
        eventManager.addListener(EventType.DATABASE_READY, args -> {
            DatabaseManager databaseManager = (DatabaseManager) args[0];
            onDatabaseReady(databaseManager);
        });
        eventManager.addListener(EventType.DATABASE_UPDATE, args -> {
            DatabaseManager databaseManager = (DatabaseManager) args[0];
            onDatabaseUpdate(databaseManager);
        });
        eventManager.addListener(EventType.LOADED, args -> {
            onLoaded();
        });
        eventManager.addListener(EventType.DISABLE, args -> {
            onDisable();
        });
        eventManager.addListener(EventType.EXECUTE_SERVER_COMMAND, args -> {
            String commandToExecute = (String) args[0];
            onExecuteServerCommand(commandToExecute);
        });
        eventManager.addListener(EventType.EXECUTE_SERVER_COMMAND_RESULT, args -> {
            Boolean status = (Boolean) args[0];
            String message = (String) args[1];
            onExecuteServerCommandResult(status, message);
        });
        eventManager.addListener(EventType.MESSAGE_TO_MINECRAFT, args -> {
            String message = (String) args[0];
            onMessageToMinecraft(message);
        });
    }

    public void onCommandExecute(Player player, String commandExecuted) {}
    public void onAlert(String message) {}
    public void onBotReady(JDA discordBot) {}
    public void onBotUpdate(GuildDiscordModel discord) {}
    public void onPlayerJoin(Player player) {}
    public void onPlayerChat(Player player, String message) {}
    public void onPlayerLeave(Player player) {}
    public void onLogging(String message) {}
    public void onLoggingWarning(String message) {}
    public void onLoggingError(String message) {}
    public void onLoggingDebug(String message) {}
    public void onLoggingSuccess(String message) {}
    public void onDatabaseReady(DatabaseManager databaseManager) {}
    public void onDatabaseUpdate(DatabaseManager databaseManager) {}
    public void onLoaded() {}
    public void onDisable() {}
    public void onExecuteServerCommand(String commandToExecute) {}
    public void onExecuteServerCommandResult(Boolean status, String message) {}
    public void onMessageToMinecraft(String message) {}
}
