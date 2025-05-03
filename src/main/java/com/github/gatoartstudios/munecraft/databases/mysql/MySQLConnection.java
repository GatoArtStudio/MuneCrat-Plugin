package com.github.gatoartstudios.munecraft.databases.mysql;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.models.SQL;

import java.sql.Connection;

public class MySQLConnection {
    private final String URL;
    private final SQL sql;

    private final Connection connection;

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
}
