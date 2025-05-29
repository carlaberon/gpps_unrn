package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanDeTrabajo {
    private int id;
    private int idProyecto;
    private int cantHoras;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Actividad> actividades = new ArrayList<>();
    private String recursos;
    private boolean aprobado;

    public PlanDeTrabajo(int proyectoAsignado, LocalDate fechaInicio, LocalDate fechaFin, List<Actividad> actividades, String recursos) {
        this.idProyecto = proyectoAsignado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.actividades = actividades;
        this.aprobado = false;
        this.recursos = recursos;
    }

    public List<Actividad> actividades() {
        return actividades;
    }

    public int getId() {
        return id;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public double calcularProgreso() {
        if (actividades == null || actividades.isEmpty()) return 0.0;
        long realizadas = actividades.stream().filter(Actividad::finalizado).count();
        return ((double) realizadas / actividades.size()) * 100;
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

    public void agregarActividad(Actividad act) {
        actividades.add(act);
    }

    public String recursos() {
        return recursos;
    }

}
