package model;

import java.time.LocalDate;
//import database.ConvenioDAOJDBC;
//import database.DataBaseConnectionException;

public class Administrador extends Usuario {
	private ConvenioDAO dao;

    public Administrador(int id, String nombreUsuario, String contrasenia, String nombre, String email, ConvenioDAO dao) {
        super(id, nombreUsuario, contrasenia, nombre, email);
        this.dao = dao;
    }

    public Convenio generarConvenio(int idEntidad, int idProyecto, String descripcion, 
            LocalDate fechaInicio, LocalDate fechaFin){
    	
    	Convenio convenio = new Convenio(idEntidad, idProyecto, descripcion, fechaInicio, fechaFin);
    	this.dao.create(convenio);
    	return convenio;
}

public void cargarConvenioFirmado(Convenio convenio, byte[] archivoPdf)
              {
	convenio.setArchivoPdf(archivoPdf);
	convenio.activar(); 
	this.dao.updateArchivo(convenio);
	}
}