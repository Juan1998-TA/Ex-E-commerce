package org.exc.sezionecommerciale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String HOST     = "localhost";
    private static final int    PORT     = 5432;
    private static final String DB_NAME  = "DB_E-commerce";
    private static final String USER     = "ecom_user";
    private static final String PASSWORD = "ecom_pass";
    private static final String URL      = String.format(
        "jdbc:postgresql://%s:%d/%s", HOST, PORT, DB_NAME);

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL non trovato.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}