package com.github.gatoartstudios.munecraft.databases.mysql;

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
                throw new IllegalArgumentException("SQL file not found in resources: " + resourcePath);
            }

            String sqlContent;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                sqlContent = reader.lines().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            String[] sqlStatements = sqlContent.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String stmt : sqlStatements) {
                    stmt = stmt.trim();
                    if (!stmt.isEmpty()) {
                        statement.execute(stmt + ";");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
