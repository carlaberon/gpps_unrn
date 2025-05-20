package database;

import model.Estudiante;
import model.GestorDeUsuarios;
import model.Tutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServicioDePersistenciaGestionUsuarios implements GestorDeUsuarios {
    Connection conn;

    public ServicioDePersistenciaGestionUsuarios(Connection conn) {
        try {
            this.conn = Conn.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Estudiante> obtenerTodosEstudiantes() {
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
    public List<Tutor> obtenerTodosTutores() {
        List<Tutor> tutores = new ArrayList<>();
        String sql = "SELECT u.idUsuario, u.nombreUsuario, u.nombre, u.email, t.tipo " +
                "FROM Usuario u " +
                "JOIN Tutor t ON u.idUsuario = t.id_Usuario";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tutores.add(new Tutor(
                        rs.getInt("idUsuario"),
                        rs.getString("nombreUsuario"),
                        "", // No obtenemos la contraseña por seguridad
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("tipo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tutores;
    }
}
