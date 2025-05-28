package model;

import database.DataBaseConnectionException;

public interface ConvenioDAO {
	void create(Convenio convenio)throws DataBaseConnectionException;
	void updateArchivo(Convenio convenio)throws DataBaseConnectionException;

	Convenio buscarPorId(int id)throws DataBaseConnectionException;
	byte[] obtenerArchivoPdfPorId(int id) throws DataBaseConnectionException;
}
