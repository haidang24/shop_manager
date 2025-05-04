package com.shopmanager.utils;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility class to run database migrations
 */
public class DatabaseMigrationUtil {

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Add image_url column to products table if it doesn't exist
            String sql = "ALTER TABLE products ADD COLUMN IF NOT EXISTS image_url VARCHAR(255)";
            stmt.executeUpdate(sql);

            System.out.println("Database migration completed successfully");

        } catch (Exception e) {
            System.err.println("Error during database migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}