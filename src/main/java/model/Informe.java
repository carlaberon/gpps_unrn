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

    public Informe(String descripcion, String tipo) {
        this.descripcion = descripcion;
        this.tipo = tipo;
    }
}
