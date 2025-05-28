package model;

import java.time.LocalDate;

public class Actividad {
    private String descripcion;
    private LocalDate fechaInicio;
    private int horas;
    private boolean finalizado;

    public Actividad(String descripcion, LocalDate fechaInicio, int horas, boolean finalizado) {
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.horas = horas;
        this.finalizado = finalizado;
    }

    public LocalDate fechaInicio() {
        return fechaInicio;
    }

    public String descripcion() {
        return descripcion;
    }

    public int horas() {
        return horas;
    }

    public boolean finalizado() {
        return finalizado;
    }
}
