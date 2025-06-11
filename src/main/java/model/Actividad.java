package model;

import java.time.LocalDate;


public class Actividad {
    private int idActividad;
    private String descripcion;
    private LocalDate fechaInicio;
    private int horas;
    private boolean finalizado;
    private int idInforme;
    private boolean requiereInforme;

    public Actividad(String descripcion, LocalDate fechaInicio, int horas, boolean finalizado, boolean requiereInforme) {

        if (esDatoNulo(descripcion) || esDatoVacio(descripcion)) {
            throw new RuntimeException("La descripción no puede ser nula o vacía.");
        }
        if (esFechaNula(fechaInicio)) {
            throw new RuntimeException("La fecha de inicio no puede ser nula.");
        }
        if (horas <= 0) {
            throw new RuntimeException("Las horas deben ser un número positivo.");
        }


        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.horas = horas;
        this.finalizado = finalizado;
        this.requiereInforme = requiereInforme;
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

    public int getIdInforme() {
        return idInforme;
    }

    public void setIdInforme(int idInforme) {
        this.idInforme = idInforme;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "idActividad=" + idActividad +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", horas=" + horas +
                ", finalizado=" + finalizado +
                ", idInforme=" + idInforme +
                '}';
    }

    public boolean requiereInforme() {
        return requiereInforme;
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

}
