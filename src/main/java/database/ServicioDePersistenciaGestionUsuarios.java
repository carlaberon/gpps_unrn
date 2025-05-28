package database;

import model.Administrador;
import model.Director;
import model.Estudiante;
import model.GestorDeUsuarios;
import model.Tutor;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.nombre, u.email, t.tipo " +
                "FROM usuarios u " +
                "JOIN tutores t ON u.id_usuario = t.id_usuario";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tutores.add(new Tutor(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
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
    @Override
    public Usuario find(String nombreUsuario, String contrasenia) throws SQLException {
        Usuario usuario = null;

        Connection conn = Conn.getConnection();
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nombreUsuario);
        stmt.setString(2, contrasenia);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String email = rs.getString("email");
            String tipo = rs.getString("tipo"); // debe existir esta columna

            if ("administrador".equalsIgnoreCase(tipo)) {
                usuario = new Administrador(id, nombreUsuario, contrasenia, nombre, email);
            } else if ("estudiante".equalsIgnoreCase(tipo)) {
                String legajo = rs.getString("legajo");
                boolean regular = rs.getBoolean("regular");
                String direccionPostal = rs.getString("direccion_postal");

                usuario = new Estudiante(id, nombreUsuario, contrasenia, nombre, email, legajo, regular, direccionPostal);
            } else {
                throw new SQLException("Tipo de usuario desconocido.");
            }
        } else {
            throw new SQLException("Usuario o contraseña incorrectos.");
        }

        return usuario;
    }


    @Override
    public List<Director> obtenerTodosDirector() {
        //completar
        return List.of();

    }
}

