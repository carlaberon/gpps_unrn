package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.ActividadDAO;

public class ActividadDAOJDBC implements ActividadDAO{
	@Override
	public void actualizarEstadoActividad(int idActividad, boolean estado) throws SQLException {
	    String sql = "UPDATE actividades SET estado = ? WHERE id_actividad = ?";
	    try (Connection conn = Conn.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	    	stmt.setBoolean(1, estado);
	        stmt.setInt(2, idActividad);
	        stmt.executeUpdate();
	    }
	}
	@Override
	public void marcarComoCompletado(int idActividad) throws SQLException {
        String sql = "UPDATE actividades SET estado = true WHERE id_actividad = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idActividad);
            stmt.executeUpdate();
        }
    }
}
