package model;

import java.time.LocalDate;
import database.ConvenioDAO;
import database.DataBaseConnectionException;

public class Convenio {
    private int id;
    private int idEntidad;
    private int idProyecto;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo = false;
    private byte[] archivoPdf;

    public Convenio(int idEntidad, int idProyecto, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idEntidad = idEntidad;
        this.idProyecto = idProyecto;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public void generarConvenio(ConvenioDAO convenioDAO) throws DataBaseConnectionException {
        convenioDAO.create(this);
    }
    public void setArchivoPdf(byte[] archivoPdf) { 
    	this.archivoPdf = archivoPdf; 
    }
    public byte[] getArchivoPdf() { 
    	return archivoPdf; 
    }
    public int getId() { 
    	return id; 
    }
    
    public void activar() { 
    	this.activo = true; 
    }
    public boolean isActivo() { 
    	return activo; 
    }
    
	public int getIdEntidad() {
		return this.idEntidad;
	}
	
    public int getIdProyecto() { 
    	return idProyecto; 
    }

    public String getDescripcion() { 
    	return descripcion; 
    }

    public LocalDate getFechaInicio() { 
    	return fechaInicio; 
    }

    public LocalDate getFechaFin() { 
    	return fechaFin; 
    }
    public void setId(int id) {
        this.id = id;
    }

}