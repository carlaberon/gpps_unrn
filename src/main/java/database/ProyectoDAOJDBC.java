package database;

import model.Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProyectoDAOJDBC implements ProyectoDAO {
    private Connection conn;

    public ProyectoDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void guardar(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO Proyecto (id_proyecto, nombre, descripcion, areaDeInteres, estado, docenteSupervisor, idUsuario_director, idUsuario_estudiante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, proyecto.getId());
            stmt.setString(2, proyecto.getNombre());
            stmt.setString(3, proyecto.getDescripcion());
            stmt.setString(4, proyecto.getAreaDeInteres());
            stmt.setBoolean(5, proyecto.getEstado());
            stmt.setInt(6, proyecto.getDocenteSupervisor().getId());
            stmt.setInt(7, proyecto.getDirector().getId());
            stmt.setInt(8, proyecto.getEstudiante().getId());
            stmt.executeUpdate();
        }
    }
}
