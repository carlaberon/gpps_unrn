package main;

import front.VentanaLogin;
import model.GestorDeConvenios;
import model.GestorDeUsuarios;
import model.GestorDeProyectos;
import database.ServicioDePersistenciaGestionUsuarios;
import database.ServicioDePersistenciaGestionProyectos;
import database.ServicioDePersistenciaGestionConvenios;
import database.Conn;

public class MainPrincipal {
    public static void main(String[] args) {
        try {
            // Initialize database connection
            Conn.connect();
            
            GestorDeUsuarios gestorDeUsuarios = new ServicioDePersistenciaGestionUsuarios(Conn.getConnection());
            GestorDeProyectos gestorDeProyectos = new ServicioDePersistenciaGestionProyectos();
            GestorDeConvenios gestorDeConvenios = new ServicioDePersistenciaGestionConvenios();

            javax.swing.SwingUtilities.invokeLater(() -> {
                new VentanaLogin(gestorDeUsuarios, gestorDeProyectos, gestorDeConvenios).setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }
}
