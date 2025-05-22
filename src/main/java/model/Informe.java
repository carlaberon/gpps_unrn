package model;

import java.time.LocalDate;

public class Informe {

    private int id;
    private String descripcion;
    private LocalDate fechaEntrega;
    private int valoracionInforme;
    private boolean estado; // recibido true


    public Informe(int id, String descripcion){

        this.id = id;
        this.descripcion = descripcion;
        this.fechaEntrega = LocalDate.now();
        this.estado = false;
    }

    public void setValoracionInforme(int valoracionInforme){
        this.valoracionInforme = valoracionInforme;
        this.estado = true;
    }
}
