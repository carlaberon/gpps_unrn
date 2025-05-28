package model;

import java.time.LocalDate;


public class Actividad {
    private int idActividad;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public boolean isEstado() {
        return finalizado;
    }

    public void setEstado(boolean estado) {
        this.finalizado = estado;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int int1) {
        this.idActividad = int1;

    }


}
