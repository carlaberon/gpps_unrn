package main;

import database.ServicioDePersistenciaGestionProyectos;
import model.GestorDeProyectos;
import ui.SeleccionarProyecto;

import javax.swing.*;
import java.sql.Connection;

public class Main4 {
    public static void main(String[] args) throws Exception {
        Connection conn = null;

        GestorDeProyectos dao = new ServicioDePersistenciaGestionProyectos();
        SwingUtilities.invokeLater(() -> {
            new SeleccionarProyecto(dao).setVisible(true);
        });

    }
}

