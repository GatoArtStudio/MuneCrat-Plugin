package com.github.gatoartstudios.munecraft.databases.mysql.DAO;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.interfaces.ICrud;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.PlayerModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for player data using MySQL.
 */
public class MySQLPlayerDAO implements ICrud<UUID, PlayerModel> {
    private final Connection connection;

    /**
     * Constructor for MySQLPlayerDAO.
     * Initializes the database connection and creates the players table if it doesn't exist.
     */
    public MySQLPlayerDAO() {
        this.connection = DatabaseManager.getInstance(null).getConnection();
        LoggerCustom.info("Crud for players loaded as a new instance");
    }

    // Constructor for testing
    public MySQLPlayerDAO(Connection connection) {
        this.connection = connection;
        LoggerCustom.info("Crud for players loaded as a new instance");
    }

    /**
     * Retrieves all player data from the database.
     *
     * @return A list of PlayerData objects.
     */
    @Override
    public List<PlayerModel> getAll() {
        List<PlayerModel> list = new ArrayList<>();
        String sqlQuery = "SELECT * FROM player";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                list.add(mapResultSetToPlayerData(rs));
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error al obtener la lista de jugadores: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Deletes a player data entry from the database by UUID.
     *
     * @param id The UUID of the player to delete.
     */
    @Override
    public void delete(UUID id) {
        String sqlQuery = "DELETE FROM player WHERE uuid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error al borrar el jugador con UUID " + id.toString() + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a player data entry in the database.
     *
     * @param entity The PlayerData object containing updated data.
     */
    @Override
    public void update(PlayerModel entity) {
        String sqlQuery = "UPDATE player SET uuid=?, minecraft_name=?, ip=?, login_at=?, logout_at=?, is_active=?, inventory=?, inventory_staff=?, location=?, is_premium=?, is_mode_staff=?, is_mode_staffchat=? WHERE id_player=?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setObject(1, entity.getUuid() != null ? entity.getUuid().toString() : null, Types.VARCHAR);
            stmt.setString(2, entity.getMinecraftName());
            stmt.setObject(3, entity.getIp(), Types.VARCHAR);
            stmt.setObject(4, entity.getLoginAt() != null ? Timestamp.valueOf(entity.getLoginAt()) : null, Types.TIMESTAMP);
            stmt.setObject(5, entity.getLogoutAt() != null ? Timestamp.valueOf(entity.getLogoutAt()) : null, Types.TIMESTAMP);
            stmt.setObject(6, entity.isActive(), Types.BOOLEAN);
            stmt.setObject(7, entity.getInventory(), Types.LONGVARCHAR);
            stmt.setObject(8, entity.getInventoryStaff(), Types.LONGVARCHAR);
            stmt.setObject(9, entity.getLocation(), Types.LONGVARCHAR);
            stmt.setObject(10, entity.isPremium(), Types.BOOLEAN);
            stmt.setObject(11, entity.isModeStaff(), Types.BOOLEAN);
            stmt.setObject(12, entity.isModeStaffChat(), Types.BOOLEAN);

            stmt.setLong(13, entity.getIdPlayer());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a player data entry from the database by UUID.
     *
     * @param id The UUID of the player to read.
     * @return The PlayerData object, or null if not found.
     */
    @Override
    public PlayerModel read(UUID id) {
        String sqlQuery = "SELECT * FROM player WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, id.toString());
            ResultSet resultPlayer = stmt.executeQuery();
            if (resultPlayer.next()) {
                return mapResultSetToPlayerData(resultPlayer);
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public PlayerModel readByMinecraftName(String playerName) {
        String sqlQuery = "SELECT * FROM player WHERE minecraft_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, playerName);
            ResultSet resultPlayer = stmt.executeQuery();
            if (resultPlayer.next()) {
                return mapResultSetToPlayerData(resultPlayer);
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Creates a new player data entry in the database.
     *
     * @param entity The PlayerData object to create.
     */
    @Override
    public void create(PlayerModel entity) {
        String sqlQuery = "INSERT INTO player (uuid, minecraft_name, ip, login_at, logout_at, is_active, inventory, inventory_staff, location, is_premium, is_mode_staff, is_mode_staffchat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setObject(1, entity.getUuid() != null ? entity.getUuid().toString() : null, Types.VARCHAR);
            stmt.setString(2, entity.getMinecraftName());
            stmt.setObject(3, entity.getIp(), Types.VARCHAR);
            stmt.setObject(4, entity.getLoginAt() != null ? Timestamp.valueOf(entity.getLoginAt()) : null, Types.TIMESTAMP);
            stmt.setObject(5, entity.getLogoutAt() != null ? Timestamp.valueOf(entity.getLogoutAt()) : null, Types.TIMESTAMP);
            stmt.setObject(6, entity.isActive(), Types.BOOLEAN);
            stmt.setObject(7, entity.getInventory(), Types.LONGVARCHAR);
            stmt.setObject(8, entity.getInventoryStaff(), Types.LONGVARCHAR);
            stmt.setObject(9, entity.getLocation(), Types.LONGVARCHAR);
            stmt.setObject(10, entity.isPremium(), Types.BOOLEAN);
            stmt.setObject(11, entity.isModeStaff(), Types.BOOLEAN);
            stmt.setObject(12, entity.isModeStaffChat(), Types.BOOLEAN);

            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Maps a ResultSet row to a PlayerData object.
     *
     * @param rs The ResultSet containing player data.
     * @return The PlayerData object.
     * @throws SQLException If an SQL error occurs.
     */
    private PlayerModel mapResultSetToPlayerData(ResultSet rs) throws SQLException {
        return new PlayerModel(
                rs.getLong("id_player"),
                rs.getObject("uuid", String.class) != null ? UUID.fromString(rs.getString("uuid")) : null,
                rs.getString("minecraft_name"),
                rs.getObject("ip", String.class),
                rs.getObject("login_at", Timestamp.class) != null ? rs.getTimestamp("login_at").toLocalDateTime() : null,
                rs.getObject("logout_at", Timestamp.class) != null ? rs.getTimestamp("logout_at").toLocalDateTime() : null,
                rs.getObject("is_active", Boolean.class),
                rs.getObject("inventory", String.class),
                rs.getObject("inventory_staff", String.class),
                rs.getObject("location", String.class),
                rs.getObject("is_premium", Boolean.class),
                rs.getObject("is_mode_staff", Boolean.class),
                rs.getObject("is_mode_staffchat", Boolean.class)
        );
    }
}
