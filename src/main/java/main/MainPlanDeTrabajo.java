package main;

import database.ServicioDePersistenciaGestionProyectos;
import ui.VerProyecto;
import ui.VerProyectoInformes;

public class MainPlanDeTrabajo {

    public static void main(String[] args) {
//        new CrearPlanTrabajo(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);
        new VerProyecto(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);
        new VerProyectoInformes(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);
//        new ProyectosACargo(new ServicioDePersistenciaGestionProyectos(), 5);
//        new AprobacionDeProyectos(new ServicioDePersistenciaGestionProyectos());
//        new VerProyectoDirector(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);

    }


}
