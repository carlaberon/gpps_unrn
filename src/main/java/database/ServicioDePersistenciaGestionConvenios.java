package database;

import model.Convenio;
import model.EntidadColaboradora;
import model.GestorDeConvenios;
import model.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServicioDePersistenciaGestionConvenios implements GestorDeConvenios {

    public void create(Convenio convenio) {
        String sqlEstudiante = "SELECT id_usuario FROM estudiantes WHERE id_proyecto = " + convenio.getIdProyecto();
        String sqlInsert = "INSERT INTO convenios (id_proyecto, id_entidad, id_usuario, fecha_inicio, fecha_fin, descripcion, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             Statement stmtEst = conn.createStatement();
             ResultSet rs = stmtEst.executeQuery(sqlEstudiante)) {

            if (!rs.next()) {
                throw new RuntimeException("No se encontró un estudiante con el proyecto ID: " + convenio.getIdProyecto());
            }

            int idUsuario = rs.getInt("id_usuario");

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, convenio.getIdProyecto());
                stmt.setInt(2, convenio.getIdEntidad());
                stmt.setInt(3, idUsuario);
                stmt.setDate(4, Date.valueOf(convenio.getFechaInicio()));
                stmt.setDate(5, Date.valueOf(convenio.getFechaFin()));
                stmt.setString(6, convenio.getDescripcion());
                stmt.setBoolean(7, convenio.isActivo());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        convenio.setId(generatedKeys.getInt(1));
                    } else {
                        throw new RuntimeException("No se pudo obtener el ID generado del convenio.");
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el convenio: " + e.getMessage(), e);
        }
    }


    public void updateArchivo(Convenio convenio) {
        PreparedStatement statement;
        Connection conn;

        try {
            conn = Conn.getConnection();

            statement = conn.prepareStatement(
                    "UPDATE convenios SET archivopdf = ?, activo = ? WHERE id = ?");

            statement.setBytes(1, convenio.getArchivoPdf());
            statement.setBoolean(2, convenio.isActivo());
            statement.setInt(3, convenio.getId());

            int filas = statement.executeUpdate();

            if (filas <= 0) {
                throw new RuntimeException("No se pudo actualizar el archivo del convenio con ID: " + convenio.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el archivo del convenio: " + e.getMessage(), e);
        } finally {
            try {
                Conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }

    public Convenio buscarPorId(int id) {
        Connection conn;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            conn = Conn.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM convenios WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Convenio convenio = new Convenio(
                        rs.getInt("id_entidad_colaboradora"),
                        rs.getInt("id_proyecto"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate()
                );
                convenio.setId(rs.getInt("id"));
                convenio.setArchivoPdf(rs.getBytes("archivoPdf"));
                if (rs.getBoolean("activo")) convenio.activar();
                return convenio;
            } else {
                throw new RuntimeException("No se encontró convenio con ID: " + id);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el convenio por ID: " + e.getMessage(), e);
        } finally {
            try {
                Conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public byte[] obtenerArchivoPdfPorId(int id) {
        try {
            Connection conn = Conn.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT archivoPdf FROM convenios WHERE id = ?"
            );

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("archivoPdf");
            } else {
                throw new RuntimeException("No se encontró el convenio con ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el archivo PDF del convenio: " + e.getMessage(), e);
        } finally {
            try {
                Conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<EntidadColaboradora> obtenerTodas() throws SQLException {
        List<EntidadColaboradora> entidades = new ArrayList<>();
        String sql = "SELECT id_entidad, nombre FROM entidades";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_entidad");
                String nombre = rs.getString("nombre");

                EntidadColaboradora entidad = new EntidadColaboradora(id, nombre, nombre, nombre, nombre, nombre);
                entidad.setId(id);
                entidad.setNombre(nombre);
                entidades.add(entidad);
            }
        }
        return entidades;
    }

    @Override
    public List<Proyecto> obtenerProyectosConEstudiante() throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();

        String sql = "SELECT DISTINCT p.id_proyecto, p.nombre, p.descripcion, p.area_de_interes, " +
                "p.ubicacion, p.id_usuario_tutor_interno, p.id_usuario_tutor_externo, p.estado " +
                "FROM proyectos p " +
                "JOIN estudiantes e ON p.id_proyecto = e.id_proyecto " +
                "WHERE p.estado = TRUE " +
                "AND NOT EXISTS (SELECT 1 FROM convenios c WHERE c.id_proyecto = p.id_proyecto)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proyecto proyecto = new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado"),
                        rs.getString("area_de_interes"),
                        null,
                        null,
                        rs.getString("ubicacion")
                );
                proyectos.add(proyecto);
            }
        }
        return proyectos;
    }
}

