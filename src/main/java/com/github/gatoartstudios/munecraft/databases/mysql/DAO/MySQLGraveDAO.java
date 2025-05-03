package com.github.gatoartstudios.munecraft.databases.mysql.DAO;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.core.interfaces.ICrud;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.GraveModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLGraveDAO implements ICrud<Integer, GraveModel> {
    private final Connection connection;

    public MySQLGraveDAO() {
        this.connection = DatabaseManager.getInstance(null).getConnection();
        LoggerCustom.info("Crud for graves loaded as a new instance");
    }

    @Override
    public List<GraveModel> getAll() {
        List<GraveModel> graveModels = new ArrayList<>();
        String sqlQuery = "SELECT * FROM grave";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                graveModels.add(mapResultSetToGrave(rs));
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error retrieving graves: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return graveModels;
    }

    @Override
    public void delete(Integer id) {
        String sqlQuery = "DELETE FROM grave WHERE id_grave = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error deleting grave with ID " + id + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GraveModel entity) {
        String sqlQuery = "UPDATE grave SET uuid=?, respawn_here=?, grave_public=?, grave_duration=?, grave_location=?, grave_inventory=? WHERE id_grave=?";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, entity.getUuid().toString());
            stmt.setBoolean(2, entity.getRespawnHere());
            stmt.setBoolean(3, entity.getGravePublic());
            stmt.setInt(4, entity.getGraveDuration());
            stmt.setString(5, entity.getGraveLocation());
            stmt.setString(6, entity.getGraveInventory());
            stmt.setInt(7, entity.getIdGrave());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error updating grave: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public GraveModel read(Integer id) {
        String sqlQuery = "SELECT * FROM grave WHERE id_grave = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToGrave(rs);
            }
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error reading grave with ID " + id + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(GraveModel entity) {
        String sqlQuery = "INSERT INTO grave (uuid, respawn_here, grave_public, grave_duration, grave_location, grave_inventory) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, entity.getUuid().toString());
            stmt.setBoolean(2, entity.getRespawnHere());
            stmt.setBoolean(3, entity.getGravePublic());
            stmt.setInt(4, entity.getGraveDuration());
            stmt.setString(5, entity.getGraveLocation());
            stmt.setString(6, entity.getGraveInventory());
            stmt.executeUpdate();
        } catch (SQLException e) {
            EventDispatcher.dispatchAlert("Error creating grave: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private GraveModel mapResultSetToGrave(ResultSet rs) throws SQLException {
        return new GraveModel(
                rs.getInt("id_grave"),
                UUID.fromString(rs.getString("uuid")),
                rs.getBoolean("respawn_here"),
                rs.getBoolean("grave_public"),
                rs.getInt("grave_duration"),
                rs.getString("grave_location"),
                rs.getString("grave_inventory")
        );
    }
}
