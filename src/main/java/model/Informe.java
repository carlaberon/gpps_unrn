package model;

import java.time.LocalDate;

public class Informe {

    private int id;
    private String descripcion;
    private LocalDate fechaEntrega;
    private int valoracionInforme;
    private boolean estado; // recibido true
    private String tipo;
    private byte[] archivoEntregable;


    public Informe(int id, String descripcion, String tipo, byte[] archivoEntregable) {

        this.id = id;
        this.descripcion = descripcion;
        this.fechaEntrega = LocalDate.now();
        this.valoracionInforme = -1; // -1 indica que no se ha valorado
        this.estado = false;
        this.tipo = tipo;
        this.archivoEntregable = archivoEntregable;
    }

    public void setValoracionInforme(int valoracionInforme){
        this.valoracionInforme = valoracionInforme;
        this.estado = true;
    }
    public int id() {
        return id;
    }
    public String descripcion() {
        return descripcion;
    }
    public LocalDate fechaEntrega() {
        return fechaEntrega;
    }
    public int valoracionInforme() {
        return valoracionInforme;
    }
    public boolean estado() {
        return estado;
    }
    public String tipo() {
        return tipo;
    }
    public byte[] archivoEntregable() {
        return archivoEntregable;
    }
}
