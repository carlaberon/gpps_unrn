package model;

import java.time.LocalDate;
import database.DataBaseConnectionException;

public class Administrador extends Usuario {
	 public Administrador(int id, String nombreUsuario, String contrasenia, String nombre, String email, Rol rol) {
	        super(id, nombreUsuario, contrasenia, nombre, email, rol);
	    }

    public Convenio generarConvenio(int idEntidad, int idProyecto, String descripcion, 
            LocalDate fechaInicio, LocalDate fechaFin, ConvenioDAO dao)throws DataBaseConnectionException {
    	
    	Convenio convenio = new Convenio(idEntidad, idProyecto, descripcion, fechaInicio, fechaFin);
    	dao.create(convenio);
    	return convenio;
}

public void cargarConvenioFirmado(Convenio convenio, byte[] archivoPdf, ConvenioDAO dao)
              throws DataBaseConnectionException {
	convenio.setArchivoPdf(archivoPdf);
	convenio.activar(); 
	dao.updateArchivo(convenio); 
	}
}