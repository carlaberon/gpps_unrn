package main;

import model.GestorDeConvenios;
import model.GestorDeUsuarios;
import ui.VentanaLogin;
import model.GestorDeProyectos;
import database.ServicioDePersistenciaGestionUsuarios;
import database.ServicioDePersistenciaGestionProyectos;
import database.ServicioDePersistenciaGestionConvenios;
import database.Conn;

public class MainPrincipal {
    public static void main(String[] args) {
        try {
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
