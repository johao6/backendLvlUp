package com.levelup.backend.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilidad para crear la base de datos en RDS PostgreSQL
 * Ejecutar con: mvn exec:java -Dexec.mainClass="com.levelup.backend.utils.DatabaseCreator"
 */
public class DatabaseCreator {
    
    private static final String RDS_HOST = "db-levelup.chguglymjysp.us-east-1.rds.amazonaws.com";
    private static final String RDS_PORT = "5432";
    private static final String RDS_USER = "levelup";
    private static final String RDS_PASSWORD = "Fernandini81.";
    private static final String DEFAULT_DB = "postgres";
    private static final String NEW_DB = "levelup";
    
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("🗄️  Creador de Base de Datos RDS PostgreSQL");
        System.out.println("============================================================");
        System.out.println();
        
        try {
            createDatabase();
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
    
    private static void createDatabase() throws Exception {
        String url = String.format("jdbc:postgresql://%s:%s/%s", RDS_HOST, RDS_PORT, DEFAULT_DB);
        
        System.out.println("🔌 Conectando a RDS: " + RDS_HOST + "...");
        
        // Cargar driver PostgreSQL
        Class.forName("org.postgresql.Driver");
        
        // Conectar a la base de datos por defecto
        try (Connection conn = DriverManager.getConnection(url, RDS_USER, RDS_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            System.out.println("✅ Conexión exitosa!");
            
            // Verificar si la base de datos ya existe
            String checkQuery = String.format(
                "SELECT 1 FROM pg_database WHERE datname = '%s'", NEW_DB
            );
            
            ResultSet rs = stmt.executeQuery(checkQuery);
            boolean exists = rs.next();
            rs.close();
            
            if (exists) {
                System.out.println("ℹ️  La base de datos '" + NEW_DB + "' ya existe.");
            } else {
                System.out.println("🔨 Creando base de datos '" + NEW_DB + "'...");
                
                String createQuery = String.format(
                    "CREATE DATABASE %s OWNER %s ENCODING 'UTF8'", NEW_DB, RDS_USER
                );
                
                stmt.executeUpdate(createQuery);
                System.out.println("✅ Base de datos '" + NEW_DB + "' creada exitosamente!");
            }
            
            System.out.println("\n🎉 ¡Todo listo!");
            System.out.println("✅ Ahora puedes iniciar tu backend Spring Boot");
            System.out.println("💡 Comando: .\\mvnw.cmd spring-boot:run");
        }
    }
}

