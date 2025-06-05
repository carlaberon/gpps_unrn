package database;

import model.Proyecto;
import model.ProyectoDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAOJDBC implements ProyectoDAO {

    @Override
    public void guardar(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO proyectos (id_proyecto, nombre, descripcion, areaDeInteres, estado, docenteSupervisor, idUsuario_tutor) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, proyecto.getId());
            stmt.setString(2, proyecto.getNombre());
            stmt.setString(3, proyecto.getDescripcion());
            stmt.setString(4, proyecto.getAreaDeInteres());
            stmt.setBoolean(5, proyecto.getEstado());
            stmt.setInt(6, proyecto.getDocenteSupervisor().getId());
            stmt.setInt(7, proyecto.getTutor().getId());

            stmt.executeUpdate();
        }
    }


    public List<Proyecto> obtenerProyectosConEstudiante() throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();

        String sql = "SELECT DISTINCT p.id_proyecto, p.nombre, p.descripcion, p.area_de_interes, " +
                     "p.ubicacion, p.id_usuario_tutor_interno, p.id_usuario_tutor_externo, p.estado " +
                     "FROM proyectos p " +
                     "JOIN estudiantes e ON p.id_proyecto = e.id_proyecto " +
                     "WHERE p.estado = TRUE " +
                     "AND NOT EXISTS (SELECT 1 FROM convenios c WHERE c.id_proyecto = p.id_proyecto)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proyecto proyecto = new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado"),
                        rs.getString("area_de_interes"),
                        null, // tutor
                        null, // docenteSupervisor
                        rs.getString("ubicacion")
                );
                proyectos.add(proyecto);
            }
        }

        System.out.println("Proyectos cargados: " + proyectos.size());
        return proyectos;
    }

    public List<Proyecto> obtenerTodos() throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT id_proyecto, nombre FROM proyectos";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_proyecto");
                String nombre = rs.getString("nombre");

                Proyecto proyecto = new Proyecto(id, nombre, nombre, false, nombre, conn, conn, nombre);
                proyecto.setId(id);
                proyecto.setNombre(nombre);

                proyectos.add(proyecto);
            }
        }
        return proyectos;
    }

}
