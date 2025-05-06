package model;

import java.time.LocalDate;

public class Informe {
    public static final String PARCIAL = "Parcial";
    public static final String FINAL = "Final";
    private int id;
    private String descripcion;
    private LocalDate fechaEntrega;
    private String tipo;
    private String archivo;
    private boolean estado; //true: entregado || false: no entregado
    private int valoracionInforme;

    public Informe(int id, String descripcion, LocalDate fechaEntrega, String tipo) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaEntrega = fechaEntrega;
        this.tipo = tipo;
    }
}
