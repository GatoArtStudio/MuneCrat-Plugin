package com.github.gatoartstudios.munecraft.databases;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.event.EventListener;
import com.github.gatoartstudios.munecraft.models.SQL;

import java.sql.Connection;

public class DatabaseManager {
    /**
     * Represents a manager for database connections.
     * <p>
     * This class handles the connection to a database. It supports MySQL databases, but it's easy to add support
     * for other types of databases.
     * <p>
     * The database connection is only established when the server is ready.
     */
    private final SQL sql;
    private final Munecraft plugin;

    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Creates a new instance of the DatabaseManager.
     * <p>
     * The plugin instance is used to access the configuration of the plugin.
     *
     * @param plugin The instance of the plugin.
     */
    private DatabaseManager(Munecraft plugin) {
        this.plugin = plugin;
        this.sql = plugin.getConfigManager().getSql();


        try {
            if (sql.getType().equalsIgnoreCase("mysql")) {
                this.connection = new MySQLConnection(sql).getConnection();
            } else {
                EventDispatcher.dispatchAlert("Unknown database type: " + sql.getType());
                return;
            }

            if (this.connection == null) {
                EventDispatcher.dispatchAlert("Database connection is null");
                return;
            }

            EventDispatcher.dispatchDatabaseRedy(this);

        } catch (Exception e) {
            EventDispatcher.dispatchAlert(e.getMessage());
        }

        new EventHandler(this);
    }

    /**
     * Gets an instance of the DatabaseManager.
     * <p>
     * If the instance is null, it creates a new instance of the DatabaseManager.
     *
     * @param plugin The instance of the plugin.
     * @return The instance of the DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance(Munecraft plugin){
        if (instance == null) {
            instance = new DatabaseManager(plugin);
        }
        return instance;
    }

    /**
     * Gets the database connection.
     * <p>
     * The connection is only established when the server is ready.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Represents a class that listens for events related to the database.
     * <p>
     * When the server is ready, it dispatches the event "DATABASE_UPDATE" with the instance of the DatabaseManager.
     */
    private static class EventHandler extends EventListener {
        private DatabaseManager databaseManager;

        /**
         * Creates a new instance of the EventHandler.
         * <p>
         * The DatabaseManager instance is used to dispatch the event "DATABASE_UPDATE".
         *
         * @param databaseManager The instance of the DatabaseManager.
         */
        public EventHandler(DatabaseManager databaseManager) {
            this.databaseManager = databaseManager;
        }

        /**
         * Called when the server is ready.
         * <p>
         * It dispatches the event "DATABASE_UPDATE" with the instance of the DatabaseManager.
         */
        @Override
        public void onLoaded() {
            EventDispatcher.dispatchDatabaseUpdate(databaseManager);
        }
    }
}
