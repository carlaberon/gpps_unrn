package main;

import database.ProyectoDAOJDBC;
import front.SeleccionarProyecto;
import model.ProyectoDAO;
import java.sql.Connection;

import javax.swing.SwingUtilities;

import database.Conn;

public class Main4 {
    public static void main(String[] args) throws Exception {
        try (Connection conn = Conn.getConnection()) {
            ProyectoDAO dao = new ProyectoDAOJDBC();
            SwingUtilities.invokeLater(() -> {
                new SeleccionarProyecto(dao).setVisible(true);
            });
            
            
        }
    }
}
