package database;

import model.GestorDeProyectos;
import model.Informe;
import model.Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//el import...dependencia

public class ServicioDePersistenciaGestionProyectos implements GestorDeProyectos {
    private Connection conn;

    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos (nombre, descripcion, area_de_interes, ubicacion, id_usuario_tutor_interno, id_usuario_tutor_externo, id_usuario_estudiante, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
