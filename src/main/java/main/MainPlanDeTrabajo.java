package main;

import database.ServicioDePersistenciaGestionProyectos;
import ui.VerProyecto;

public class MainPlanDeTrabajo {

    public static void main(String[] args) {
//        new CrearPlanTrabajo(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);
        new VerProyecto(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);
    }


}
