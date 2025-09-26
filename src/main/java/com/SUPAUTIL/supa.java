package com.SUPAUTIL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class supa {
    private static final String URL = "jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:5432/postgres?user=postgres.yibhllavyovhbjaxwynu&password=Anton056780990";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
