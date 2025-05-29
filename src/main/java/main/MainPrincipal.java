package main;

import front.VentanaLogin;
import model.GestorDeUsuarios;
import model.GestorDeProyectos;
import model.ConvenioDAO;
import database.ServicioDePersistenciaGestionUsuarios;
import database.ServicioDePersistenciaGestionProyectos;
import database.ConvenioDAOJDBC;

public class MainPrincipal {
    public static void main(String[] args) {
        try {
            GestorDeUsuarios gestorDeUsuarios = new ServicioDePersistenciaGestionUsuarios(null);
            GestorDeProyectos gestorDeProyectos = new ServicioDePersistenciaGestionProyectos();
            ConvenioDAO convenioDAO = new ConvenioDAOJDBC();

            javax.swing.SwingUtilities.invokeLater(() -> {
                new VentanaLogin(gestorDeUsuarios, gestorDeProyectos, convenioDAO).setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }
}
