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
        if (esDatoNulo(descripcion) || esDatoVacio(descripcion))
            throw new RuntimeException("El campo descripción no puede estar vacio");
        this.descripcion = descripcion;
        this.fechaEntrega = LocalDate.now();
        this.valoracionInforme = -1; // -1 indica que no se ha valorado
        this.estado = false;
        this.tipo = tipo;
        if (noExisteArchivo(archivoEntregable))
            throw new RuntimeException("Debe subir un archivo!");
        this.archivoEntregable = archivoEntregable;
    }

    public void setValoracionInforme(int valoracionInforme) {
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

    private boolean esDatoNulo(String dato) {
        return dato == null;
    }

    private boolean esDatoVacio(String dato) {
        return dato.equals("");
    }

    private boolean noExisteArchivo(byte[] archivo) {
        return (archivo == null || archivo.length == 0);
    }
}
