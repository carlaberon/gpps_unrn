package database;

import model.Estudiante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOJDBC implements EstudianteDAO {
    private Connection conn;

    public EstudianteDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT u.idUsuario, u.nombreUsuario, u.nombre, u.email, e.legajo, e.esRegular " +
                    "FROM Usuario u " +
                    "JOIN Estudiante e ON u.idUsuario = e.id_Usuario";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                estudiantes.add(new Estudiante(
                    rs.getInt("idUsuario"),
                    rs.getString("nombreUsuario"),
                    "", // No obtenemos la contraseña por seguridad
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("legajo"),
                    rs.getBoolean("esRegular"),
                    "" // No tenemos dirección en la nueva estructura
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estudiantes;
    }

    @Override
    public Estudiante obtenerPorId(int id) {
        String sql = "SELECT u.idUsuario, u.nombreUsuario, u.nombre, u.email, e.legajo, e.esRegular " +
                    "FROM Usuario u " +
                    "JOIN Estudiante e ON u.idUsuario = e.id_Usuario " +
                    "WHERE u.idUsuario = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Estudiante(
                    rs.getInt("idUsuario"),
                    rs.getString("nombreUsuario"),
                    "", // No obtenemos la contraseña por seguridad
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("legajo"),
                    rs.getBoolean("esRegular"),
                    "" // No tenemos dirección en la nueva estructura
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    	public void seleccionarProyecto(Estudiante estudiante) throws DataBaseConnectionException {
    		if (estudiante.getProyectoSeleccionado() != null) {
    		    try (Connection conn = Conn.getConnection()) {
    		        PreparedStatement stmt = conn.prepareStatement(
    		            "UPDATE estudiante SET id_proyecto = ? WHERE id = ?");
    		        stmt.setInt(1, estudiante.getProyectoSeleccionado().getId());
    		        stmt.setInt(2, estudiante.getId());
    		        stmt.executeUpdate();
    		    } catch (SQLException e) {
    		        throw new DataBaseConnectionException("Error al asignar proyecto al estudiante.");
    		    }
    		}
    	}
    
}
