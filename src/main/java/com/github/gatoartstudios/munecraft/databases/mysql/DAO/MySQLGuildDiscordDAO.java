package com.github.gatoartstudios.munecraft.databases.mysql.DAO;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.interfaces.ICrud;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.GuildDiscordModel;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLGuildDiscordDAO implements ICrud<Long, GuildDiscordModel> {
    private final Connection connection;

    public MySQLGuildDiscordDAO() {
        this.connection = DatabaseManager.getInstance(null).getConnection();
        LoggerCustom.info("Crud for GuildDiscord loaded as a new instance");
    }

    @Override
    public List<GuildDiscordModel> getAll() {
        List<GuildDiscordModel> list = new ArrayList<>();
        String sqlQuery = "SELECT * FROM guild_discord";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                list.add(mapResultSetToGuildDiscord(rs));
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error fetching GuildDiscord list: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM guild_discord WHERE guild_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error deleting GuildDiscord with ID " + id + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GuildDiscordModel entity) {
        String sqlQuery = "UPDATE guild_discord SET log_channel_id=?, warning_channel_id=?, announcement_channel_id=?, "
                        + "sanction_channel_id=?, report_channel_id=?, message_channel_id=?, command_channel_id=?, "
                        + "alert_channel_id=?, player_activity_channel_id=?, log_user_verified=? WHERE guild_id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setObject(1, entity.getLogChannelId(), Types.BIGINT);
            stmt.setObject(2, entity.getWarningChannelId(), Types.BIGINT);
            stmt.setObject(3, entity.getAnnouncementChannelId(), Types.BIGINT);
            stmt.setObject(4, entity.getSanctionChannelId(), Types.BIGINT);
            stmt.setObject(5, entity.getReportChannelId(), Types.BIGINT);
            stmt.setObject(6, entity.getMessageChannelId(), Types.BIGINT);
            stmt.setObject(7, entity.getCommandChannelId(), Types.BIGINT);
            stmt.setObject(8, entity.getAlertChannelId(), Types.BIGINT);
            stmt.setObject(9, entity.getPlayerActivityChannelId(), Types.BIGINT);
            stmt.setObject(10, entity.getLogUserVerifiedId(), Types.BIGINT);
            stmt.setLong(11, entity.getGuildId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public GuildDiscordModel read(Long id) {
        String sqlQuery = "SELECT * FROM guild_discord WHERE guild_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToGuildDiscord(rs);
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(GuildDiscordModel entity) {
        String sqlQuery = "INSERT INTO guild_discord (guild_id, log_channel_id, warning_channel_id, announcement_channel_id, "
                        + "sanction_channel_id, report_channel_id, message_channel_id, command_channel_id, "
                        + "alert_channel_id, player_activity_channel_id, log_user_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, entity.getGuildId());
            stmt.setObject(2, entity.getLogChannelId(), Types.BIGINT);
            stmt.setObject(3, entity.getWarningChannelId(), Types.BIGINT);
            stmt.setObject(4, entity.getAnnouncementChannelId(), Types.BIGINT);
            stmt.setObject(5, entity.getSanctionChannelId(), Types.BIGINT);
            stmt.setObject(6, entity.getReportChannelId(), Types.BIGINT);
            stmt.setObject(7, entity.getMessageChannelId(), Types.BIGINT);
            stmt.setObject(8, entity.getCommandChannelId(), Types.BIGINT);
            stmt.setObject(9, entity.getAlertChannelId(), Types.BIGINT);
            stmt.setObject(10, entity.getPlayerActivityChannelId(), Types.BIGINT);
            stmt.setObject(11, entity.getLogUserVerifiedId(), Types.BIGINT);
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private GuildDiscordModel mapResultSetToGuildDiscord(ResultSet rs) throws SQLException {
        return new GuildDiscordModel(
                rs.getLong("guild_id"),
                rs.getObject("log_channel_id", Long.class),
                rs.getObject("warning_channel_id", Long.class),
                rs.getObject("announcement_channel_id", Long.class),
                rs.getObject("sanction_channel_id", Long.class),
                rs.getObject("report_channel_id", Long.class),
                rs.getObject("message_channel_id", Long.class),
                rs.getObject("command_channel_id", Long.class),
                rs.getObject("alert_channel_id", Long.class),
                rs.getObject("player_activity_channel_id", Long.class),
                rs.getObject("log_user_verified", Long.class)
        );
    }
}
