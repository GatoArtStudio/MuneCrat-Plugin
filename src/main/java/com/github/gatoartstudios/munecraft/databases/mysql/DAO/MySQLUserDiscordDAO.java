package com.github.gatoartstudios.munecraft.databases.mysql.DAO;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.interfaces.ICrud;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.UserDiscordModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserDiscordDAO implements ICrud<Long, UserDiscordModel> {
    private final Connection connection;

    public MySQLUserDiscordDAO() {
        this.connection = DatabaseManager.getInstance(null).getConnection();
        LoggerCustom.info("Crud for UserDiscord loaded as a new instance");
    }

    @Override
    public void create(UserDiscordModel entity) {
        String sqlQuery = "INSERT INTO user_discord (id_discord, discord_name, password, minecraft_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, entity.getIdDiscord());
            stmt.setString(2, entity.getDiscordName());
            stmt.setString(3, entity.getPassword());
            stmt.setString(4, entity.getMinecraftName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error creating UserDiscord: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDiscordModel read(Long id) {
        String sqlQuery = "SELECT * FROM user_discord WHERE id_discord = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUserDiscord(rs);
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error reading UserDiscord with ID " + id + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public UserDiscordModel readByMinecraftName(String minecraftName) {
        String sqlQuery = "SELECT * FROM user_discord WHERE minecraft_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, minecraftName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUserDiscord(rs);
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error reading minecraft_name with name " + minecraftName + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(UserDiscordModel entity) {
        String sqlQuery = "UPDATE user_discord SET discord_name = ?, password = ?, minecraft_name = ? WHERE id_discord = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, entity.getDiscordName());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getMinecraftName());
            stmt.setLong(4, entity.getIdDiscord());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error updating UserDiscord with ID " + entity.getIdDiscord() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM user_discord WHERE id_discord = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error deleting UserDiscord with ID " + id + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDiscordModel> getAll() {
        List<UserDiscordModel> list = new ArrayList<>();
        String sqlQuery = "SELECT * FROM user_discord";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                list.add(mapResultSetToUserDiscord(rs));
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error fetching UserDiscord list: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    private UserDiscordModel mapResultSetToUserDiscord(ResultSet rs) throws SQLException {
        return new UserDiscordModel(
                rs.getLong("id_discord"),
                rs.getString("discord_name"),
                rs.getString("password"),
                rs.getString("minecraft_name")
        );
    }
}
