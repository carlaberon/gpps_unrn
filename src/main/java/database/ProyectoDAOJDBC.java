package database;

import model.Proyecto;
import model.ProyectoDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAOJDBC implements ProyectoDAO {
    private Connection conn;

    public ProyectoDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void guardar(Proyecto proyecto)  {
        String sql = "INSERT INTO Proyecto (id_proyecto, nombre, descripcion, areaDeInteres, estado, docenteSupervisor, idUsuario_director, idUsuario_estudiante) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, proyecto.getId());
            stmt.setString(2, proyecto.getNombre());
            stmt.setString(3, proyecto.getDescripcion());
            stmt.setString(4, proyecto.getAreaDeInteres());
            stmt.setBoolean(5, proyecto.getEstado());
            stmt.setInt(6, proyecto.getDocenteSupervisor().getId());
            stmt.setInt(7, proyecto.getDirector().getId());
            stmt.setInt(8, proyecto.getEstudiante().getId());
            stmt.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException("",e);
        }
    }
    public List<Proyecto> obtenerProyectos() {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.areaDeInteres, "
                + "p.idUsuario_estudiante, p.idUsuario_director, p.docenteSupervisor "
                + "FROM Proyecto p "
                + "WHERE p.estado = true AND p.idUsuario_estudiante IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proyecto proyecto = new Proyecto(
                    rs.getInt("id_proyecto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getBoolean("estado"),
                    rs.getString("areaDeInteres"),
                    null, // estudiante aún no asignado
                    null, // cargar director si es necesario
                    null  // cargar tutor si es necesario
                );
                proyectos.add(proyecto);
            }
        }catch (Exception e){
            throw new RuntimeException("", e);
        }
        System.out.println("Proyectos cargados: " + proyectos.size());
        return proyectos;
    }
}
