package database;

import model.GestorDeProyectos;
import model.Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
//el import...dependencia

public class ServicioDePersistenciaGestionProyectos implements GestorDeProyectos {
    private Connection conn;

    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {
        //aca implemento
        //ahora muestro cómo hacer las pruebas sin tener la implementacion

    }

    @Override
    public void registrarAsignacionDocenteTutor(int idProyecto, int idDocente, int idTutor) {
        String sql = "UPDATE proyecto SET docenteSupervisor = ?, idUsuario_tutor = ? WHERE id_proyecto = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, idDocente);   // suponiendo que Tutor tiene getId()
            statement.setInt(2, idTutor);
            statement.setInt(3, idProyecto);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Problema con la persistencia");
        }

    }
}
