package main;

import database.ServicioDePersistenciaGestionProyectos;
import ui.CrearPlanTrabajo;

public class MainPlanDeTrabajo {

    public static void main(String[] args) {
        new CrearPlanTrabajo(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);

    }


}
