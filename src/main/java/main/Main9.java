package main;

import javax.swing.SwingUtilities;

import database.ServicioDePersistenciaGestionConvenios;
import front.VentanaPrincipal;
import model.GestorDeConvenios;
import model.GestorDeUsuarios;
import model.Administrador;
import model.GestorDeProyectos;



import database.ServicioDePersistenciaGestionProyectos;
import database.ServicioDePersistenciaGestionUsuarios;

import java.sql.Connection;
public class Main9 {
    public static void main(String[] args) {
    	Connection conn = null;
    	GestorDeUsuarios gestorUsuarios = new ServicioDePersistenciaGestionUsuarios(conn);
    	GestorDeProyectos gestorProyectos = new ServicioDePersistenciaGestionProyectos();
        Administrador admin = new Administrador(0, null, null, null, null, null);
        GestorDeConvenios gestorDeConvenios = new ServicioDePersistenciaGestionConvenios();

        SwingUtilities.invokeLater(() -> {
        	VentanaPrincipal ventana = new VentanaPrincipal(gestorUsuarios, gestorProyectos, admin, gestorDeConvenios);
            ventana.setVisible(true);
        });
    }
}
