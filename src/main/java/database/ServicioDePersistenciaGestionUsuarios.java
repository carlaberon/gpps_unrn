package database;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDePersistenciaGestionUsuarios implements GestorDeUsuarios {
    private Connection conn;

    public ServicioDePersistenciaGestionUsuarios(Connection conn) {
        this.conn = conn;
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
                        "",
                        rs.getString("nombre"),
                        rs.getString("email"),
                        null, rs.getString("legajo"),
                        rs.getBoolean("esRegular"),
                        ""
                        , 0
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
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.nombre, u.email, t.tipo " +
                "FROM usuarios u " +
                "JOIN tutores t ON u.id_usuario = t.id_usuario";

        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tutores.add(new Tutor(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        "",
                        rs.getString("nombre"),
                        rs.getString("email"),
                        null,
                        rs.getString("tipo")
                ));
            }
            return tutores;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los tutores: " + e.getMessage(), e);
        }
    }


    public Usuario buscarUsuario(String nombreUsuario, String contrasena) throws SQLException {
        String sql = """
                    SELECT u.id_usuario, u.nombre_usuario, u.contrasenia,
                           u.nombre, u.email,
                           r.codigo AS rol_codigo, r.nombre AS rol_nombre
                    FROM usuarios u
                    JOIN usuarios_roles ur ON u.id_usuario = ur.id_usuario
                    JOIN roles r ON ur.codigo = r.codigo
                    WHERE u.nombre_usuario = ? AND u.contrasenia = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    int codigoRol = rs.getInt("rol_codigo");
                    String nombreRol = rs.getString("rol_nombre");
                    Rol rol = new Rol(codigoRol, nombreRol);

                    return switch (nombreRol.toLowerCase()) {
                        case "administrador" -> new Administrador(id, nombreUsuario, contrasena, nombre, email, rol);
                        case "estudiante" ->
                                new Estudiante(id, nombreUsuario, contrasena, nombre, email, rol, null, null, null, codigoRol);
                        case "director" -> new Director(id, nombreUsuario, contrasena, nombre, email, rol);
                        case "tutor" -> new Tutor(id, nombreUsuario, contrasena, nombre, email, rol, null);
                        default -> throw new SQLException("Rol desconocido: " + nombreRol);
                    };
                } else {
                    throw new SQLException("Usuario o contraseña incorrectos.");
                }
            }
        }
    }

    public Integer obtenerIdProyectoEstudiante(int idEstudiante) {
        String sql = "SELECT id_proyecto FROM estudiantes WHERE id_usuario = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEstudiante);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idProyecto = rs.getInt("id_proyecto");
                    return rs.wasNull() ? null : idProyecto;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el ID del proyecto del estudiante", e);
        }

        return null;
    }


    @Override
    public List<Director> obtenerTodosDirector() {
        return List.of();
    }
}

