package database;

import model.GestorDeProyectos;
import model.Informe;
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
        String sql = "INSERT INTO proyecto (nombre, descripcion, area_de_interes, ubicacion, id_usuario_tutor_interno, id_usuario_tutor_externo, id_usuario_estudiante, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, proyecto.getNombre());
            statement.setString(2, proyecto.getDescripcion());
            statement.setString(3, proyecto.getAreaDeInteres());
            statement.setString(4, proyecto.getUbicacion());
            statement.setInt(5, proyecto.getIdUsuarioTutorInterno());
            statement.setInt(6, proyecto.getIdUsuarioTutorExterno());
            statement.setInt(7, proyecto.getIdUsuarioEstudiante());
            statement.setBoolean(8, proyecto.getEstado());

            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void registrarAsignacionTutorInterno(int idProyecto, int idTutorInterno) {
        String sql = "UPDATE proyecto SET idUsuario_tutorInterno = ? WHERE id_proyecto = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idTutorInterno);
            statement.setInt(2, idProyecto);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Problema con la persistencia");
        }

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
        String sql = "INSERT INTO proyecto (nombre, descripcion, area_de_interes, ubicacion, estado, id_usuario_tutor_interno, id_usuario_tutor_externo) " +
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
        String sql = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.areaDeInteres, " +
                "p.docenteSupervisor, p.idUsuario_director, p.idUsuario_estudiante " +
                "FROM proyecto p " +
                "WHERE p.estado = TRUE AND p.idUsuario_estudiante IS NULL";

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

    public void cargarInforme(Informe informeParcial) {

        String sql = "INSERT INTO informes (descripcion, fecha_entrega, tipo, valoracionInforme, estado, archivo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, informeParcial.descripcion());
            statement.setDate(2, java.sql.Date.valueOf(informeParcial.fechaEntrega()));
            statement.setString(3, informeParcial.tipo());
            statement.setInt(4, informeParcial.valoracionInforme());
            statement.setBoolean(5, informeParcial.estado());
            statement.setNull(6, java.sql.Types.BLOB); // Asumiendo que no se carga un archivo en este momento
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
