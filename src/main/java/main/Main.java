package main;

import java.sql.Connection;
import java.sql.SQLException;

import database.Conn;

public class Main {
    public static void main(String[] args) {
            try {
                Connection conexion = Conn.getConnection();
                if (conexion != null && !conexion.isClosed()) {
                    System.out.println("✅ Conexión exitosa a la base de datos.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar: " + e.getMessage());
            }
        }
    }