package model;

import java.sql.SQLException;

public interface PlanDAO {
	PlanDeTrabajo obtenerPorProyecto(int idProyecto) throws SQLException;
}
