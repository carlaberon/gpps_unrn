package model;

import java.util.List;

public class PlanDeTrabajo {
    private Proyecto proyectoAsignado;
    private int cantHoras;
    private boolean estado;
    private List<Actividad> actividades;

    public PlanDeTrabajo(List<Actividad> actividades, boolean estado, int cantHoras) {
        this.actividades = actividades;
        this.estado = estado;
        this.cantHoras = cantHoras;
    }
}
