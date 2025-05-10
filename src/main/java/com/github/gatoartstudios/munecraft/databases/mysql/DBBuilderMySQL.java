package com.github.gatoartstudios.munecraft.databases.mysql;

import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DBBuilderMySQL {
    public static void executeSqlFileFromResources(Connection connection, String resourcePath) {
        try (InputStream inputStream = DBBuilderMySQL.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                LoggerCustom.error("SQL file not found in resources: " + resourcePath);
                return;
            }

            String sqlContent;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                sqlContent = reader.lines().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                LoggerCustom.error("Error in build database: " + e.getMessage());
                return;
            }

            String[] sqlStatements = sqlContent.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String stmt : sqlStatements) {
                    stmt = stmt.trim();
                    if (!stmt.isEmpty()) {
                        try {
                            statement.execute(stmt + ";");
                        } catch (Exception e) {
                            LoggerCustom.warning("Error executing statement: " + stmt);
                            LoggerCustom.warning("SQL Error: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LoggerCustom.error("Error in build database: " + e.getMessage());
        }
    }
}
