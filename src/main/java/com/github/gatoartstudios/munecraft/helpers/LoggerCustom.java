package com.github.gatoartstudios.munecraft.helpers;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import okhttp3.Dispatcher;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class LoggerCustom {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[0;91m";
    private static final String ANSI_GREEN = "\u001B[0;92m";
    private static final String ANSI_YELLOW = "\u001B[0;93m";
    private static final String ANSI_BLUE = "\u001B[0;94m";
    private static final String ANSI_CYAN = "\u001B[0;96m";
    private static final String ANSI_PURPLE = "\u001B[0;95m";
    private static final String prefix = ANSI_PURPLE + "[MuneCraft] " + ANSI_RESET;
    private static final String errorPrefix = ANSI_RED + "[ERROR] ";
    private static final String warningPrefix = ANSI_YELLOW + "[WARNING] ";
    private static final String infoPrefix = ANSI_CYAN + "[INFO] ";
    private static final String debugPrefix = ANSI_BLUE + "[DEBUG] ";
    private static final String successPrefix = ANSI_GREEN + "[SUCCESS] " + ANSI_RESET;

    private static Logger getLogger() {
        try {
            return Bukkit.getLogger();
        } catch (Throwable t) {
            // Estamos en test, o Bukkit no cargó
            return null;
        }
    }

    private static void logRaw(String msg, String typePrefix) {
        Logger logger = getLogger();
        if (logger != null) {
            switch (typePrefix) {
                case infoPrefix:
                    logger.info(prefix + infoPrefix + msg + ANSI_RESET);
                    EventDispatcher.dispatchLogging("[Plugin] [Info] " + msg);
                    break;
                case warningPrefix:
                    logger.warning(prefix + warningPrefix + msg + ANSI_RESET);
                    EventDispatcher.dispatchLogging("[Plugin] [Warning] " + msg);
                    break;
                case errorPrefix:
                    logger.severe(prefix + errorPrefix + msg + ANSI_RESET);
                    EventDispatcher.dispatchLogging("[Plugin] [Error] " + msg);
                    break;
                case debugPrefix:
                    logger.info(prefix + debugPrefix + msg + ANSI_RESET);
                    EventDispatcher.dispatchLogging("[Plugin] [Debug] " + msg);
                    break;
                case successPrefix:
                    logger.info(prefix + successPrefix + msg + ANSI_RESET);
                    EventDispatcher.dispatchLogging("[Plugin] [Success] " + msg);
                    break;
                default:
                    logger.info(prefix + msg + ANSI_RESET);
                    EventDispatcher.dispatchLogging("[Plugin] " + msg);
                    break;
            }
        } else {
            System.out.println(msg);
        }
    }

    /**
     * Logs an informational message to the console and dispatches a logging event.
     *
     * @param message the informational message to be logged
     */
    public static void info(String message) {
        logRaw(message, infoPrefix);
    }

    /**
     * Logs a warning message to the console and dispatches a logging event.
     *
     * @param message the warning message to be logged
     */
    public static void warning(String message) {
        logRaw(message, warningPrefix);
    }

    /**
     * Logs an error message to the console and dispatches a logging event.
     *
     * @param message the error message to be logged
     */
    public static void error(String message) {
        logRaw(message, errorPrefix);
    }

    /**
     * Logs a debug message to the console and dispatches a logging event.
     *
     * @param message the debug message to be logged
     */
    public static void debug(String message) {
        logRaw(message, debugPrefix);
    }

    /**
     * Logs a success message to the console and dispatches a logging event.
     *
     * @param message the success message to be logged
     */
    public static void success(String message) {
        logRaw(message, successPrefix);
    }

    /**
     * Logs a raw message to the console and dispatches a logging event.
     *
     * @param message the raw message to be logged
     */
    public static void raw(String message) {
        logRaw(message, "");
    }
}
