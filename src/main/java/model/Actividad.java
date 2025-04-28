package model;

import java.time.LocalDate;

public class Actividad {
    private String descripcion;
    private LocalDate fechaInicio;
    private int horas;
    private boolean estado;

    public Actividad(String descripcion, LocalDate fechaInicio, int horas, boolean estado) {
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.horas = horas;
        this.estado = estado;
    }
}
