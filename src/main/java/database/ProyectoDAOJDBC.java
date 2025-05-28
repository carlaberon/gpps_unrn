//package database;
//
//import model.Proyecto;
//import model.ProyectoDAO;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProyectoDAOJDBC implements ProyectoDAO {
//
//    @Override
//    public void guardar(Proyecto proyecto) throws SQLException {
//        String sql = "INSERT INTO Proyecto (id_proyecto, nombre, descripcion, areaDeInteres, estado, docenteSupervisor, idUsuario_tutor) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        try (Connection conn = Conn.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, proyecto.getId());
//            stmt.setString(2, proyecto.getNombre());
//            stmt.setString(3, proyecto.getDescripcion());
//            stmt.setString(4, proyecto.getAreaDeInteres());
//            stmt.setBoolean(5, proyecto.getEstado());
//            stmt.setInt(6, proyecto.getDocenteSupervisor().getId());
//            stmt.setInt(7, proyecto.getTutor().getId());
//
//            stmt.executeUpdate();
//        }
//    }
//
//
//    public List<Proyecto> obtenerProyectos() throws SQLException {
//        List<Proyecto> proyectos = new ArrayList<>();
//        String sql = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.areaDeInteres, "
//                + "p.ubicacion, p.idUsuario_estudiante, p.idUsuario_tutor, p.docenteSupervisor "
//                + "FROM Proyecto p "
//                + "WHERE p.estado = false AND p.idUsuario_estudiante IS NULL "
//                + "AND NOT EXISTS (SELECT 1 FROM Convenio c WHERE c.id_proyecto = p.id_proyecto)";
//
//        try (Connection conn = Conn.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                Proyecto proyecto = new Proyecto(
//                        rs.getInt("id_proyecto"),
//                        rs.getString("nombre"),
//                        rs.getString("descripcion"),
//                        rs.getBoolean("estado"),
//                        rs.getString("areaDeInteres"),
//                        null, // estudiante aún no asignado
//                        null, // tutor aún no cargado
//                        null, // docente aún no cargado
//                        rs.getString("ubicacion")
//                );
//                proyectos.add(proyecto);
//            }
//        }
//
//        return proyectos;
//    }
//
//    public List<Proyecto> obtenerTodos() throws SQLException {
//        List<Proyecto> proyectos = new ArrayList<>();
//        String sql = "SELECT id_proyecto, nombre FROM Proyecto";
//
//        try (Connection conn = Conn.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                int id = rs.getInt("id_proyecto");
//                String nombre = rs.getString("nombre");
//
//                Proyecto proyecto = new Proyecto(id, nombre, nombre, null, nombre, null, null, null);
//                proyecto.setId(id);
//                proyecto.setNombre(nombre);
//
//                proyectos.add(proyecto);
//            }
//        }
//        return proyectos;
//    }
//}
//
