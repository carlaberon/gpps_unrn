package model;

import java.sql.SQLException;

public interface ActividadDAO {
    void actualizarEstadoActividad(int idActividad, boolean estado) throws SQLException;
    void marcarComoCompletado(int idActividad) throws SQLException;
}