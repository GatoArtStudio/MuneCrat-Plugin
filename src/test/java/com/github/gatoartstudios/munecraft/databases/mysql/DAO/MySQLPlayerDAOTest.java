package com.github.gatoartstudios.munecraft.databases.mysql.DAO;

import com.github.gatoartstudios.munecraft.databases.mysql.DBBuilderMySQL;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySQLPlayerDAOTest {

    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    private static MySQLPlayerDAO dao;

    @BeforeAll
    static void setup() throws Exception {
        mysql.start();
        Connection connection = DriverManager.getConnection(
                mysql.getJdbcUrl(),
                mysql.getUsername(),
                mysql.getPassword()
        );

        DBBuilderMySQL.executeSqlFileFromResources(connection, "database/schema.sql");

        // Inyectamos la conexión a la base de datos en el DAO
        dao = new MySQLPlayerDAO(connection); // sin conexión
    }

    @Test
    @Order(1)
    void testCreateAndReadPlayer() {
        UUID uuid = UUID.randomUUID();

        PlayerModel model = new PlayerModel(
                uuid,
                "test_player"
        );

        PlayerModel model2 = new PlayerModel(
                "test_player2"
        );

        PlayerModel model3 = new PlayerModel(
                "test_player3"
        );

        dao.create(model);
        dao.create(model2);
        dao.create(model3);

        PlayerModel fetched = dao.read(uuid);

        assertNotNull(fetched);
        assertEquals("test_player", fetched.getMinecraftName());
    }

    @Test
    @Order(3)
    void testUpdate() {
        PlayerModel player = dao.readByMinecraftName("test_player");
        player.setInventory("updated inventory");
        dao.update(player);

        PlayerModel updated = dao.read(player.getUuid());
        assertEquals("updated inventory", updated.getInventory());
    }

    @Test
    @Order(4)
    void testDelete() {
        PlayerModel player = dao.readByMinecraftName("test_player");
        dao.delete(player.getUuid());

        assertNull(dao.read(player.getUuid()));
    }

    @AfterAll
    static void teardown() {
        mysql.stop();
    }
}
