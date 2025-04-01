package com.github.gatoartstudios.munecraft.core.event;

import com.github.gatoartstudios.munecraft.core.enums.EventType;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.models.Discord;
import net.dv8tion.jda.api.JDA;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventDispatcher {
    private static final Event eventManager = Event.getInstance();

    public static void dispatchCommandExecute(Player player, String commandExecuted) {
        eventManager.emit(EventType.COMMAND_EXECUTE, player, commandExecuted);
    }

    public static void dispatchAlert(String message) {
        eventManager.emit(EventType.ALERT, message);
    }

    public static void dispatchBotReady(JDA discordBot) {
        eventManager.emit(EventType.BOT_READY, discordBot);
    }

    public static void dispatchBotUpdate(Discord discord) {
        eventManager.emit(EventType.BOT_UPDATE, discord);
    }

    public static void dispatchPlayerJoin(Player player) {
        eventManager.emit(EventType.PLAYER_JOIN, player);
    }

    public static void dispatchPlayerLeave(Player player) {
        eventManager.emit(EventType.PLAYER_LEAVE, player);
    }

    public static void dispatchPlayerChat(Player player, String message) {
        eventManager.emit(EventType.PLAYER_CHAT, player, message);
    }

    public static void dispatchLogging(String message) {
        eventManager.emit(EventType.LOGGING, message);
    }

    public static void dispatchLoggingWarning(String message) {
        eventManager.emit(EventType.LOGGING_WARNING, message);
    }

    public static void dispatchLoggingError(String message) {
        eventManager.emit(EventType.LOGGING_ERROR, message);
    }

    public static void dispatchLoggingDebug(String message) {
        eventManager.emit(EventType.LOGGING_DEBUG, message);
    }

    public static void dispatchLoggingSuccess(String message) {
        eventManager.emit(EventType.LOGGING_SUCCESS, message);
    }

    public static void dispatchDatabaseRedy(DatabaseManager databaseManager) {
        eventManager.emit(EventType.DATABASE_READY, databaseManager);
    }

    public static void dispatchDatabaseUpdate(DatabaseManager databaseManager) {
        eventManager.emit(EventType.DATABASE_UPDATE, databaseManager);
    }

    public static void dispatchLoaded() {
        eventManager.emit(EventType.LOADED);
    }

    public static void dispatchExecuteServerCommand(String commandToExecute) {
        eventManager.emit(EventType.EXECUTE_SERVER_COMMAND, commandToExecute);
    }

    public static void dispatchExecuteServerCommandResult(Boolean status, String message) {
        eventManager.emit(EventType.EXECUTE_SERVER_COMMAND_RESULT, status, message);
    }
}
