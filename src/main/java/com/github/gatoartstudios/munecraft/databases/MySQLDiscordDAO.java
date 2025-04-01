package com.github.gatoartstudios.munecraft.databases;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.interfaces.ICrud;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.Discord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLDiscordDAO implements ICrud<Discord> {
    private final Connection connection;

    /**
     * Constructor for the MySQLDiscordDAO class.
     * This constructor creates an instance of the MySQLDiscordDAO class and generates the table discord in the database if it doesn't exist.
     * The table discord is used to store the configuration of the discord bot.
     */
    public MySQLDiscordDAO() {
        this.connection = DatabaseManager.getInstance(null).getConnection();

        // Create the table discord in the database if it doesn't exist
        createTable();

        // Print a message to the console indicating that the Crud for discord was loaded as a new instance
        LoggerCustom.info("Crud for discord loaded as a new instance");
    }

    /**
     * Creates the table discord in the database if it doesn't exist.
     * This table is used to store the configuration of the discord bot.
     */
    private void createTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS discord ("
                + "guild_id BIGINT NOT NULL," // The guild id of the discord server
                + "log_channel_id BIGINT NOT NULL," // The channel id where the logs are sent
                + "warning_channel_id BIGINT NOT NULL," // The channel id where the warnings are sent
                + "announcement_channel_id BIGINT NOT NULL," // The channel id where the announcements are sent
                + "sanction_channel_id BIGINT NOT NULL," // The channel id where the sanctions are sent
                + "report_channel_id BIGINT NOT NULL," // The channel id where the reports are sent
                + "message_channel_id BIGINT NOT NULL," // The channel id where the messages are sent
                + "command_channel_id BIGINT NOT NULL," // The channel id where the commands are executed
                + "alert_channel_id BIGINT NOT NULL," // The channel id where the alerts are sent
                + "player_activity_channel_id BIGINT NOT NULL," // The channel id where the player activity is sent
                + "PRIMARY KEY (guild_id)" // The primary key of the table is the guild id
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sqlQuery);
            LoggerCustom.success("The discord table was created");
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a list of all records in the database.
     *
     * @return The list of records.
     * @throws RuntimeException If any SQL error occurs.
     */
    @Override
    public List<Discord> getAll() {
        List<Discord> list = new ArrayList<>();
        String sqlQuery = "SELECT * FROM discord";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                list.add(mapResultSetToDiscord(rs));
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error al obtener la lista de configuraciones de Discord: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Deletes a record from the database.
     *
     * @param id The ID of the record to delete.
     * @throws RuntimeException If any SQL error occurs.
     */
    @Override
    public void delete(long id) {
        String sqlQuery = "DELETE FROM discord WHERE guild_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error al borrar la configuracion de discord para el servidor con ID " + id + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a record in the database.
     *
     * @param entity The entity to update.
     * @throws RuntimeException If any SQL error occurs.
     */
    @Override
    public void update(Discord entity) {
        String sqlQuery = "UPDATE discord SET log_channel_id=?, warning_channel_id=?, announcement_channel_id=?, "
                        + "sanction_channel_id=?, report_channel_id=?, message_channel_id=?, command_channel_id=?, "
                        + "alert_channel_id=?, player_activity_channel_id=? WHERE guild_id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            // Set the values for the prepared statement
            stmt.setLong(1, entity.getLogChannelId());
            stmt.setLong(2, entity.getWarningChannelId());
            stmt.setLong(3, entity.getAnnouncementChannelId());
            stmt.setLong(4, entity.getSanctionChannelId());
            stmt.setLong(5, entity.getReportChannelId());
            stmt.setLong(6, entity.getMessageChannelId());
            stmt.setLong(7, entity.getCommandChannelId());
            stmt.setLong(8, entity.getAlertChannelId());
            stmt.setLong(9, entity.getPlayerActivityChannelId());
            stmt.setLong(10, entity.getGuildId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a record in the database by its ID.
     *
     * @param id The ID to search for.
     * @return The found record, or null if not found.
     * @throws RuntimeException If any SQL error occurs.
     */
    @Override
    public Discord read(long id) {
        String sqlQuery = "SELECT * FROM discord WHERE guild_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDiscord(rs);
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
        return null; // Returns null if the record is not found.
    }

    /**
     * Creates a new record in the database.
     *
     * @param entity The entity to create.
     * @throws RuntimeException If any SQL error occurs.
     */
    @Override
    public void create(Discord entity) {
        String sqlQuery = "INSERT INTO discord (guild_id, log_channel_id, warning_channel_id, announcement_channel_id, "
                        + "sanction_channel_id, report_channel_id, message_channel_id, command_channel_id, "
                        + "alert_channel_id, player_activity_channel_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, entity.getGuildId());
            stmt.setLong(2, entity.getLogChannelId());
            stmt.setLong(3, entity.getWarningChannelId());
            stmt.setLong(4, entity.getAnnouncementChannelId());
            stmt.setLong(5, entity.getSanctionChannelId());
            stmt.setLong(6, entity.getReportChannelId());
            stmt.setLong(7, entity.getMessageChannelId());
            stmt.setLong(8, entity.getCommandChannelId());
            stmt.setLong(9, entity.getAlertChannelId());
            stmt.setLong(10, entity.getPlayerActivityChannelId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Maps the result set to a Discord object.
     *
     * @param rs The result set to map.
     * @return A Discord object with the mapped data.
     * @throws SQLException If any SQL error occurs.
     */
    private Discord mapResultSetToDiscord(ResultSet rs) throws SQLException {
        return new Discord(
                rs.getLong("guild_id"),
                rs.getLong("log_channel_id"),
                rs.getLong("warning_channel_id"),
                rs.getLong("announcement_channel_id"),
                rs.getLong("sanction_channel_id"),
                rs.getLong("report_channel_id"),
                rs.getLong("message_channel_id"),
                rs.getLong("command_channel_id"),
                rs.getLong("alert_channel_id"),
                rs.getLong("player_activity_channel_id")
        );
    }
}
