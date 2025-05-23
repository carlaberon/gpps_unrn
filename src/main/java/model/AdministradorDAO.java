package model;

import java.io.InputStream;

import database.DataBaseConnectionException;

public interface AdministradorDAO {
	void cargarArchivoConvenio(int idConvenio, InputStream archivoPDF)throws DataBaseConnectionException;
}
