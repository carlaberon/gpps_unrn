package model;

import java.time.LocalDate;
import java.util.List;

public class PlanDeTrabajo {
    private int proyectoAsignado;
    private boolean aprobado;
    private List<Actividad> actividades;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    public PlanDeTrabajo(int idProyecto, List<Actividad> actividades, LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.proyectoAsignado = proyectoAsignado;
        this.actividades = actividades;
        this.aprobado = false;
    }

    public List<Actividad> actividades() {
        return actividades;
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

    public int cantHoras() {
        int totalHoras = 0;
        for (Actividad actividad : actividades) {
            totalHoras += actividad.horas();
        }
        return totalHoras;
    }

    public LocalDate fechaFin() {
        return fechaFin;
    }

    public LocalDate fechaInicio() {
        return fechaInicio;
    }

}
