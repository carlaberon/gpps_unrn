package model;

import java.time.LocalDate;

public class Convenio {
    private int id;
    private int idEntidad;
    private int idProyecto;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Convenio(int idEntidad, int idProyecto, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idEntidad = idEntidad;
        this.idProyecto = idProyecto;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public void generarConvenio(ConvenioDAO convenioDAO)
        convenioDAO.create(this);
    }