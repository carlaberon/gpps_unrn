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
        if (esFechaNula(fechaInicio) || esFechaNula(fechaFin)) {
            throw new RuntimeException("Las fechas de inicio y fin no pueden ser nulas");
        }

        if (fechaInicio.isAfter(fechaFin))
            throw new RuntimeException("La fecha de fin no puede ser antes de la fecha de inicio");


        if (actividades == null || actividades.isEmpty()) {
            throw new RuntimeException("Debe haber al menos una actividad en el plan de trabajo");
        }

        if (esDatoNulo(recursos) || esDatoVacio(recursos)) {
            throw new RuntimeException("Los recursos no pueden ser nulos o vacíos");
        }

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

    private boolean esDatoVacio(String dato) {
        return dato.equals("");
    }

    private boolean esDatoNulo(String dato) {
        return dato == null;
    }

    private boolean esFechaNula(LocalDate dato) {
        return dato == null;
    }

    public int porcentajeDeFinalizado() {
        long finalizadas = actividades.stream()
                .filter(Actividad::finalizado)
                .count();

        return (int) ((finalizadas * 100.0) / actividades.size());
    }


}
