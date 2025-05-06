package com.github.gatoartstudios.munecraft.databases.mysql;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.models.SQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MySQLConnection {
    private final String URL;
    private final SQL sql;

    private final Connection connection;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    /**
     * Creates a connection to a MySQL database.
     *
     * @param sql the database configuration
     */
    public MySQLConnection(SQL sql) {
        this.sql = sql;

        URL = "jdbc:mysql://" + sql.getHost() + ":" + sql.getPort() + "/" + sql.getDatabase();
        try {
            this.connection = java.sql.DriverManager.getConnection(URL, sql.getUser(), sql.getPassword());
            startKeepAliveTask();
        } catch (Exception e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the connection to the MySQL database.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Creates a looping task in another thread, which keeps the connection
     * to the database active by performing a query every 5 minutes.
     */
    private void startKeepAliveTask() {
        Runnable keepAlive = () -> {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SELECT 1");
            } catch (SQLException e) {
                EventDispatcher.dispatchAlert("Error in the connection maintenance task: " + e.getMessage());
            }
        };

        scheduledExecutorService.scheduleAtFixedRate(keepAlive, 5, 5, TimeUnit.MINUTES);
    }
}
