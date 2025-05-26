package database;

import model.GestorDeProyectos;
import model.Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//el import...dependencia

public class ServicioDePersistenciaGestionProyectos implements GestorDeProyectos {
    private Connection conn;

    public ServicioDePersistenciaGestionProyectos(Connection conn) {
        try {
            this.conn = Conn.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {
        //aca implemento
        //ahora muestro cómo hacer las pruebas sin tener la implementacion

    }

    @Override
    public void guardar(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO Proyecto (id_proyecto, nombre, descripcion, areaDeInteres, ubicacion, estado, idUsuario_tutorInterno, idUsuario_tutorExterno, idUsuario_estudiante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, proyecto.getId());
            stmt.setString(2, proyecto.getNombre());
            stmt.setString(3, proyecto.getDescripcion());
            stmt.setString(4, proyecto.getAreaDeInteres());
            stmt.setString(5, proyecto.getUbicacion());
            stmt.setBoolean(6, proyecto.getEstado());
            stmt.setInt(7, proyecto.getDocenteSupervisor().getId());
            stmt.setInt(8, proyecto.getTutor().getId());
            stmt.setInt(9, proyecto.getEstudiante().getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void guardarSinEstudiante(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO proyectos (nombre, descripcion, area_de_interes, ubicacion, estado, id_usuario_tutor_interno, id_usuario_tutor_externo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proyecto.getNombre());
            stmt.setString(2, proyecto.getDescripcion());
            stmt.setString(3, proyecto.getAreaDeInteres());
            stmt.setString(4, proyecto.getUbicacion());
            stmt.setBoolean(5, proyecto.getEstado());
            stmt.setInt(6, proyecto.getDocenteSupervisor().getId()); // o getTutorInterno()
            stmt.setInt(7, proyecto.getTutor().getId()); // o getTutorExterno()
            stmt.executeUpdate();
        }
    }

    public List<Proyecto> obtenerProyectos() throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.area_de_interes, " +
                "p.id_usuario_tutor_interno, p.id_usuario_tutor_externo, p.id_usuario_estudiante " +
                "FROM proyectos p " +
                "WHERE p.estado = TRUE AND p.id_usuario_estudiante IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proyecto proyecto = new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado"),
                        rs.getString("area_de_interes"),
                        null, // estudiante aún no asignado
                        null, // tutor interno (puede cargarse si querés)
                        null  // tutor externo (igual)
                );
                proyectos.add(proyecto);
            }
        }

        System.out.println("Proyectos cargados: " + proyectos.size());
        return proyectos;
    }
}
