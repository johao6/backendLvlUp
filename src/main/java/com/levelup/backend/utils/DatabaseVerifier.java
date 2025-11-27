package com.levelup.backend.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilidad para verificar tablas en RDS PostgreSQL
 */
public class DatabaseVerifier {

    private static final String RDS_HOST = "db-levelup.chguglymjysp.us-east-1.rds.amazonaws.com";
    private static final String RDS_PORT = "5432";
    private static final String RDS_USER = "levelup";
    private static final String RDS_PASSWORD = "Fernandini81.";
    private static final String DB_NAME = "levelup";

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("🔍 Verificador de Tablas RDS PostgreSQL");
        System.out.println("============================================================");
        System.out.println();

        try {
            verifyTables();
            System.out.println();
            System.out.println("============================================================");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("\n❌ Error: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
            System.out.println("============================================================");
            System.exit(1);
        }
    }

    private static void verifyTables() throws Exception {
        String url = String.format("jdbc:postgresql://%s:%s/%s", RDS_HOST, RDS_PORT, DB_NAME);

        System.out.println("🔌 Conectando a base de datos: " + DB_NAME + "...");

        Class.forName("org.postgresql.Driver");

        try (Connection conn = DriverManager.getConnection(url, RDS_USER, RDS_PASSWORD);
             Statement stmt = conn.createStatement()) {

            System.out.println("✅ Conexión exitosa!");
            System.out.println();

            // Listar todas las tablas
            String query = "SELECT table_name FROM information_schema.tables " +
                          "WHERE table_schema = 'public' ORDER BY table_name";

            ResultSet rs = stmt.executeQuery(query);

            System.out.println("📋 Tablas encontradas:");
            System.out.println("─".repeat(60));

            int count = 0;
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                System.out.println("  ✓ " + tableName);
                count++;
            }
            rs.close();

            System.out.println("─".repeat(60));
            System.out.println("Total: " + count + " tablas");

            if (count == 0) {
                System.out.println("\n⚠️  No hay tablas creadas todavía.");
                System.out.println("💡 Inicia el backend para que Hibernate cree las tablas:");
                System.out.println("   .\\mvnw.cmd spring-boot:run");
            } else {
                System.out.println("\n🎉 ¡La base de datos está lista!");
            }
        }
    }
}

