package database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.EntidadColaboradora;
import model.EntidadDAO;
public class EntidadColaboradoraJDBC implements EntidadDAO {

	public List<EntidadColaboradora> obtenerTodas() throws SQLException {
	    List<EntidadColaboradora> entidades = new ArrayList<>();
	    String sql = "SELECT id_entidad_colaboradora, nombre FROM Entidad_Colaboradora";

	    try (Connection conn = Conn.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            int id = rs.getInt("id_entidad_colaboradora");
	            String nombre = rs.getString("nombre");

	            EntidadColaboradora entidad = new EntidadColaboradora(id, nombre, nombre, nombre, nombre, nombre);
	            entidad.setId(id);
	            entidad.setNombre(nombre);
	            entidades.add(entidad);
	        }
	    }
	    return entidades;
	}
}