package model;

import java.util.List;

public class PlanDeTrabajo {
    private Proyecto proyectoAsignado;
    private int cantHoras;
    private boolean aprobado; 
    private List<Actividad> actividades;
    private String recursos;

    public PlanDeTrabajo(Proyecto proyectoAsignado, int cantHoras, List<Actividad> actividades, String recursos) {
        this.proyectoAsignado = proyectoAsignado;
        this.cantHoras = cantHoras;
        this.actividades = actividades;
        this.recursos = recursos;
        this.aprobado = false; 
    }

    public void aprobar() {
        this.aprobado = true;
    }

    public void rechazar() {
        this.aprobado = false;
    }

    public boolean estaAprobado() {
        return this.aprobado;
    }
}
