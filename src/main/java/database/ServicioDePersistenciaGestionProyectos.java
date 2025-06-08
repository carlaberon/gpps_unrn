package database;

import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//el import...dependencia

public class ServicioDePersistenciaGestionProyectos implements GestorDeProyectos {


    public ServicioDePersistenciaGestionProyectos() {

    }

    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos (nombre, descripcion, area_de_interes, ubicacion, id_usuario_tutor_interno, id_usuario_tutor_externo, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, proyecto.getNombre());
            statement.setString(2, proyecto.getDescripcion());
            statement.setString(3, proyecto.getAreaDeInteres());
            statement.setString(4, proyecto.getUbicacion());
            statement.setInt(5, proyecto.getIdUsuarioTutorInterno());
            statement.setInt(6, proyecto.getIdUsuarioTutorExterno());
            statement.setBoolean(7, proyecto.getEstado());

            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void registrarAsignacionTutorInterno(int idProyecto, int idTutorInterno) {
        String sql = "UPDATE proyectos SET id_usuario_tutor_interno = ? WHERE id_proyecto = ?";
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
    public void guardar(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO Proyecto (id_proyecto, nombre, descripcion, areaDeInteres, ubicacion, estado, idUsuario_tutorInterno, idUsuario_tutorExterno) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, proyecto.getId());
            stmt.setString(2, proyecto.getNombre());
            stmt.setString(3, proyecto.getDescripcion());
            stmt.setString(4, proyecto.getAreaDeInteres());
            stmt.setString(5, proyecto.getUbicacion());
            stmt.setBoolean(6, proyecto.getEstado());
            stmt.setInt(7, proyecto.getDocenteSupervisor().getId());
            stmt.setInt(8, proyecto.getTutor().getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public int guardarSinEstudiante(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO proyectos (nombre, descripcion, area_de_interes, ubicacion, estado, id_usuario_tutor_interno, id_usuario_tutor_externo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, proyecto.getNombre());
            stmt.setString(2, proyecto.getDescripcion());
            stmt.setString(3, proyecto.getAreaDeInteres());
            stmt.setString(4, proyecto.getUbicacion());
            stmt.setBoolean(5, proyecto.getEstado());
            stmt.setInt(6, proyecto.getDocenteSupervisor().getId()); // Tutor interno
            stmt.setInt(7, proyecto.getTutor().getId()); // Tutor externo

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Devuelve el ID generado
                } else {
                    throw new SQLException("No se pudo obtener el ID del proyecto insertado.");
                }
            }
        }
    }


    @Override
    public void cargarPlanDeTrabajo(PlanDeTrabajo plan, int idProyecto) {
        String sqlPlan = "INSERT INTO planes (id_proyecto, cant_horas, fecha_inicio, fecha_fin, estado_aprobacion) VALUES (?, ?, ?, ?, ?)";
        String sqlActividad = "INSERT INTO actividades (descripcion, fecha_inicio, horas, finalizado, id_plan) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmtPlan = conn.prepareStatement(sqlPlan, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtActividad = conn.prepareStatement(sqlActividad)) {

            // Insertar el plan
            stmtPlan.setInt(1, idProyecto);
            stmtPlan.setInt(2, plan.cantHoras());
            stmtPlan.setDate(3, Date.valueOf(plan.fechaInicio()));
            stmtPlan.setDate(4, Date.valueOf(plan.fechaFin()));
            stmtPlan.setBoolean(5, false);
            stmtPlan.executeUpdate();

            // Obtener el ID generado del plan
            ResultSet generatedKeys = stmtPlan.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new RuntimeException("No se pudo obtener el ID del plan insertado.");
            }
            int idPlan = generatedKeys.getInt(1);

            // Insertar cada actividad asociada
            for (Actividad act : plan.actividades()) {
                stmtActividad.setString(1, act.descripcion());
                stmtActividad.setDate(2, Date.valueOf(act.fechaInicio()));
                stmtActividad.setInt(3, act.horas());
                stmtActividad.setBoolean(4, false); // siempre false al crear
                stmtActividad.setInt(5, idPlan);
                stmtActividad.addBatch();
            }
            stmtActividad.executeBatch();

        } catch (Exception e) {
            throw new RuntimeException("Problema con la persistencia", e);
        }
    }

    @Override
    public void aprobarPlanDeTrabajo(int idPlan) {
        String sqlPlan = "UPDATE planes SET estado_aprobacion = ? WHERE id_plan = ?";
        String sqlProyecto = "UPDATE proyectos SET estado = 1 WHERE id_proyecto = " +
                "(SELECT id_proyecto FROM planes WHERE id_plan = ?)";

        try (Connection conn = Conn.getConnection()) {

            // Actualiza el estado del plan
            try (PreparedStatement stmtPlan = conn.prepareStatement(sqlPlan)) {
                stmtPlan.setBoolean(1, true);
                stmtPlan.setInt(2, idPlan);
                stmtPlan.executeUpdate();
            }

            // Actualiza el estado del proyecto relacionado
            try (PreparedStatement stmtProyecto = conn.prepareStatement(sqlProyecto)) {
                stmtProyecto.setInt(1, idPlan);
                stmtProyecto.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException("Problema con la persistencia", e);
        }
    }


    public List<Proyecto> obtenerProyectos() throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.area_de_interes, " +
                "p.id_usuario_tutor_interno, p.id_usuario_tutor_externo, p.ubicacion " +
                "FROM proyectos p " +
                "WHERE p.estado = TRUE " +
                "AND p.id_proyecto NOT IN (SELECT e.id_proyecto FROM estudiantes e WHERE e.id_proyecto IS NOT NULL) ";
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
                        null, // tutor
                        null, // docenteSupervisor
                        rs.getString("ubicacion")  // tutor externo (igual)
                );
                proyectos.add(proyecto);
            }
        }
        return proyectos;
    }

    @Override
    public List<Proyecto> listarProyectosRelacionados(int idUsuario) {
        List<Proyecto> proyectos = new ArrayList<>();

        String sql = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado " +
                "FROM proyectos p " +
                "WHERE (p.id_usuario_tutor_interno = ? OR p.id_usuario_tutor_externo = ?) AND p.estado = true";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Proyecto resumen = new Proyecto(
                            rs.getInt("id_proyecto"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBoolean("estado"), null, null, null, null
                    );
                    proyectos.add(resumen);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Problema de persistencia", e);
        }

        return proyectos;
    }


    @Override
    public void cargarInforme(Informe informeParcial) {
        String sql = "INSERT INTO informes (descripcion, fecha_entrega, tipo, valoracionInforme, estado, archivo) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdateActividad = "UPDATE actividades SET id_informe = ?, estado = TRUE WHERE id_actividad = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, informeParcial.descripcion());
            stmt.setDate(2, Date.valueOf(informeParcial.fechaEntrega()));
            stmt.setString(3, informeParcial.tipo());
            stmt.setInt(4, informeParcial.valoracionInforme());
            stmt.setBoolean(5, informeParcial.estado());
            stmt.setBytes(6, informeParcial.archivoEntregable());

            stmt.executeUpdate();

            // Obtener el ID generado del informe
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idInforme = generatedKeys.getInt(1);

                // Actualizar la actividad con el ID del informe y marcar como finalizada
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateActividad)) {
                    stmtUpdate.setInt(1, idInforme);
                    stmtUpdate.setInt(2, informeParcial.id());
                    stmtUpdate.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar el informe", e);
        }
    }

    @Override
    public Proyecto obtenerProyecto(int idProyecto) {
        Proyecto proyecto = null;

        String sql = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.area_de_interes, " +
                "p.id_usuario_tutor_interno, p.id_usuario_tutor_externo, p.ubicacion " +
                "FROM proyectos p " +
                "WHERE p.id_proyecto = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    proyecto = new Proyecto(
                            rs.getInt("id_proyecto"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBoolean("estado"),
                            rs.getString("area_de_interes"),
                            null, // estudiante aún no asignado
                            null, // tutor interno
                            rs.getString("ubicacion")  // tutor externo
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Problema de persistencia", e);
        }

        return proyecto;
    }

    @Override
    public PlanDeTrabajo obtenerPlan(int idProyecto) {
        PlanDeTrabajo plan = null;

        String sqlPlan = "SELECT p.id_plan, p.fecha_inicio, p.fecha_fin, p.estado_aprobacion, p.recursos " +
                "FROM planes p " +
                "WHERE p.id_proyecto = ?";

        String sqlActividades = "SELECT * FROM actividades WHERE id_plan = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlPlan)) {

            stmt.setInt(1, idProyecto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idPlan = rs.getInt("id_plan");
                    LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();
                    LocalDate fechaFin = rs.getDate("fecha_fin").toLocalDate();
                    String recursos = rs.getString("recursos");

                    List<Actividad> actividades = new ArrayList<>();

                    try (PreparedStatement stmtAct = conn.prepareStatement(sqlActividades)) {
                        stmtAct.setInt(1, idPlan);
                        try (ResultSet rsAct = stmtAct.executeQuery()) {
                            while (rsAct.next()) {
                                int idActividad = rsAct.getInt("id_actividad");
                                String descripcion = rsAct.getString("descripcion");
                                LocalDate fechaInicioAct = rsAct.getDate("fecha_inicio").toLocalDate();
                                int horas = rsAct.getInt("horas");
                                boolean finalizado = rsAct.getBoolean("estado");
                                int idInforme = rsAct.getInt("id_informe");

                                Actividad actividad = new Actividad(descripcion, fechaInicioAct, horas, finalizado);
                                actividad.setIdActividad(idActividad);
                                actividad.setIdInforme(idInforme);
                                actividades.add(actividad);
                            }
                        }
                    }

                    // Crear el plan con las actividades
                    plan = new PlanDeTrabajo(idProyecto, fechaInicio, fechaFin, actividades, recursos);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problema de persistencia");
        }

        return plan;
    }

    @Override

    public List<Proyecto> obtenerProyectosSinAprobar() {
        List<Proyecto> proyectos = new ArrayList<>();

        String sql = "SELECT id_proyecto, nombre, descripcion, estado FROM proyectos WHERE estado = 0";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proyecto resumen = new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado"), // será false porque estado = 0
                        null, null, null, null
                );
                proyectos.add(resumen);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Problema de persistencia al obtener proyectos sin aprobar", e);
        }

        return proyectos;
    }

    @Override
    public void notificarComentarioDenegacion(String comentario) {
        //Aca podriamos crear una tabla que almacene esto y notificar luego. O solamente notificar.
    }

    public List<Tutor> obtenerTutoresPorProyecto(int idProyecto) throws SQLException {
        List<Tutor> tutores = new ArrayList<>();
        String sql = """
                    SELECT u.id_usuario, u.nombre_usuario, u.nombre, u.email, t.tipo
                    FROM usuarios u
                    JOIN tutores t ON u.id_usuario = t.id_usuario
                    JOIN proyectos p ON (u.id_usuario = p.id_usuario_tutor_interno OR u.id_usuario = p.id_usuario_tutor_externo)
                    WHERE p.id_proyecto = ?
                """;

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProyecto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tutores.add(new Tutor(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_usuario"),
                            "", // No obtenemos la contraseña por seguridad
                            rs.getString("nombre"),
                            rs.getString("email"),
                            null,
                            rs.getString("tipo")
                    ));
                }
            }
        }
        return tutores;
    }

    @Override
    public List<Actividad> obtenerActividadesPorPlan(int idPlan) throws SQLException {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT id_actividad, descripcion, fecha_inicio, horas, estado, id_informe FROM actividades WHERE id_plan = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPlan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idActividad = rs.getInt("id_actividad");
                    String descripcion = rs.getString("descripcion");
                    LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();
                    int horas = rs.getInt("horas");
                    boolean finalizado = rs.getBoolean("estado");
                    int idInforme = rs.getInt("id_informe");

                    Actividad actividad = new Actividad(descripcion, fechaInicio, horas, finalizado);
                    actividad.setIdActividad(idActividad);
                    actividad.setIdInforme(idInforme);
                    actividades.add(actividad);
                }
            }
        }
        return actividades;
    }

    @Override
    public boolean asignarEstudianteAProyecto(int idEstudiante, int idProyecto) throws SQLException {
        // Verificar si el estudiante ya tiene un proyecto asignado
        String checkSql = "SELECT id_proyecto FROM estudiantes WHERE id_usuario = ? AND id_proyecto IS NOT NULL";
        try (Connection conn = Conn.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, idEstudiante);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                throw new SQLException("El estudiante ya tiene un proyecto asignado.");
            }
        }

        // Asignar estudiante al proyecto
        String sql = "UPDATE estudiantes SET id_proyecto = ? WHERE id_usuario = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);
            stmt.setInt(2, idEstudiante);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }

    }

    @Override
    public Informe obtenerInforme(int idInforme) {
        String sql = "SELECT * FROM informes WHERE id_informe = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInforme);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Informe informe = new Informe(
                            rs.getInt("id_informe"),
                            rs.getString("descripcion"),
                            rs.getString("tipo"),
                            rs.getBytes("archivo")
                    );
                    // Establecer los valores adicionales
                    informe.setValoracionInforme(rs.getInt("valoracionInforme"));
                    return informe;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener el informe: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void valorarInforme(int idInforme, int valoracion) {
        String sql = "UPDATE informes SET valoracionInforme = ? WHERE id_informe = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, valoracion);
            stmt.setInt(2, idInforme);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No se encontró el informe con ID: " + idInforme);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al valorar el informe: " + ex.getMessage());
        }
    }

}
