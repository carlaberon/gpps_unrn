package ui;

import database.ServicioDePersistenciaGestionProyectos;

public class MainPlanDeTrabajo {

    public static void main(String[] args) {
        new CrearPlanTrabajo(new ServicioDePersistenciaGestionProyectos(), 1).setVisible(true);
        
    }


}
