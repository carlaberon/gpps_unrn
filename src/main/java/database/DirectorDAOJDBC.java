package database;

import model.Director;
import model.Usuario;

import java.net.UnknownServiceException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorDAOJDBC implements DirectorDAO {
    private Connection conn;

    public DirectorDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> directores = new ArrayList<>();
        String sql = "SELECT u.idUsuario, u.nombreUsuario, u.nombre, u.email " +
                    "FROM Usuario u " +
                    "JOIN Director d ON u.idUsuario = d.id_Usuario";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                directores.add(new Director(
                    rs.getInt("idUsuario"),
                    rs.getString("nombreUsuario"),
                    "", // No obtenemos la contraseña por seguridad
                    rs.getString("nombre"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return directores;
    }

    @Override
    public Director obtenerPorId(int id) {
        String sql = "SELECT u.idUsuario, u.nombreUsuario, u.nombre, u.email " +
                    "FROM Usuario u " +
                    "JOIN Director d ON u.idUsuario = d.id_Usuario " +
                    "WHERE u.idUsuario = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Director(
                    rs.getInt("idUsuario"),
                    rs.getString("nombreUsuario"),
                    "", // No obtenemos la contraseña por seguridad
                    rs.getString("nombre"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 