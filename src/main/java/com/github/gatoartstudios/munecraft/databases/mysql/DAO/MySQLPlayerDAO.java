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
        String sqlQuery = "UPDATE player SET uuid=?, minecraft_name=?, ip=?, inventory=?, inventory_staff=?, StaffMode=?, isPremium=?, last_login=?, StaffChatMode=? WHERE uuid=?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, entity.getUuid().toString());
            stmt.setString(2, entity.getMinecraftName());
            stmt.setObject(3, entity.getIp(), Types.VARCHAR);
            stmt.setObject(4, entity.getInventory(), Types.LONGVARCHAR);
            stmt.setObject(5, entity.getInventoryStaff(), Types.LONGVARCHAR);
            stmt.setBoolean(6, entity.getStaffMode());
            stmt.setBoolean(7, entity.getPremium());
            stmt.setObject(8, entity.getLastLogin() != null ? Timestamp.valueOf(entity.getLastLogin()) : null, Types.TIMESTAMP);
            stmt.setBoolean(9, entity.getStaffChatMode());
            stmt.setString(10, entity.getUuid().toString());
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

    /**
     * Creates a new player data entry in the database.
     *
     * @param entity The PlayerData object to create.
     */
    @Override
    public void create(PlayerModel entity) {
        String sqlQuery = "INSERT INTO player (uuid, minecraft_name, ip, inventory, inventory_staff, StaffMode, isPremium, last_login, StaffChatMode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, entity.getUuid().toString());
            stmt.setString(2, entity.getMinecraftName());
            stmt.setObject(3, entity.getIp(), Types.VARCHAR);
            stmt.setObject(4, entity.getInventory(), Types.LONGVARCHAR);
            stmt.setObject(5, entity.getInventoryStaff(), Types.LONGVARCHAR);
            stmt.setBoolean(6, entity.getStaffMode());
            stmt.setBoolean(7, entity.getPremium());
            stmt.setObject(8, entity.getLastLogin() != null ? Timestamp.valueOf(entity.getLastLogin()) : null, Types.TIMESTAMP);
            stmt.setObject(9, entity.getStaffChatMode(), Types.BOOLEAN);
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
                UUID.fromString(rs.getString("uuid")),
                rs.getString("minecraft_name"),
                rs.getObject("ip", String.class),
                rs.getObject("inventory", String.class),
                rs.getObject("inventory_staff", String.class),
                rs.getBoolean("StaffMode"),
                rs.getBoolean("isPremium"),
                rs.getObject("last_login", Timestamp.class) != null ? rs.getTimestamp("last_login").toLocalDateTime() : null,
                rs.getObject("StaffChatMode", Boolean.class)
        );
    }
}
