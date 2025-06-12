package main;

import database.ServicioDePersistenciaGestionProyectos;
import ui.MenuPrincipalDirector;

public class MainDePruebasTomas {
    public static void main(String[] args) {
        new MenuPrincipalDirector(new ServicioDePersistenciaGestionProyectos()).setVisible(true);
    }
}
