package model;

import java.sql.SQLException;
import java.util.List;

public interface EntidadDAO {
	public List<EntidadColaboradora> obtenerTodas() throws SQLException ;
}
