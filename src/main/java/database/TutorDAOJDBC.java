package database;

import model.Tutor;
import model.TutorDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorDAOJDBC implements TutorDAO {
    private Connection conn;

    public TutorDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Tutor> obtenerTodos() {
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

    @Override
    public Tutor obtenerPorId(int id) {
        String sql = "SELECT u.idUsuario, u.nombreUsuario, u.nombre, u.email, t.tipo " +
                    "FROM Usuario u " +
                    "JOIN Tutor t ON u.idUsuario = t.id_Usuario " +
                    "WHERE u.idUsuario = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Tutor(
                    rs.getInt("idUsuario"),
                    rs.getString("nombreUsuario"),
                    "", // No obtenemos la contraseña por seguridad
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("tipo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 