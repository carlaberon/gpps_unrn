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
        //aca implemento
        //ahora muestro cómo hacer las pruebas sin tener la implementacion

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
