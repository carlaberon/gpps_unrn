package model;

import java.time.LocalDate;

public abstract class Actividad {
	private int idActividad;
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
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }

    public boolean isEstado() { return estado; } 
    public void setEstado(boolean estado) { this.estado = estado; }
    public int getIdActividad() { return idActividad; }

	public void setIdActividad(int int1) {
		   this.idActividad = int1;
		
	}

    
}
