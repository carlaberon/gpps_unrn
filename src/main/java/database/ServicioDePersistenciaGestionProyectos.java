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
    public int guardarProyecto(Proyecto proyecto, PlanDeTrabajo planDeTrabajo) {
        String sqlProyecto = "INSERT INTO proyectos (nombre, descripcion, area_de_interes, ubicacion, estado, id_usuario_tutor_interno, id_usuario_tutor_externo, estado_proyecto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlPlan = "INSERT INTO planes (id_proyecto, cant_horas, fecha_inicio, fecha_fin, estado_aprobacion, recursos) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlActividad = "INSERT INTO actividades (descripcion, fecha_inicio, horas, estado, requiere_informe, id_plan, id_informe) " +
                "VALUES (?, ?, ?, ?, ?, ?, NULL)"; // id_informe se deja como NULL inicialmente

        try (Connection conn = Conn.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtProyecto = conn.prepareStatement(sqlProyecto, Statement.RETURN_GENERATED_KEYS);
            ) {
                // 1. Insertar proyecto
                stmtProyecto.setString(1, proyecto.getNombre());
                stmtProyecto.setString(2, proyecto.getDescripcion());
                stmtProyecto.setString(3, proyecto.getAreaDeInteres());
                stmtProyecto.setString(4, proyecto.getUbicacion());
                stmtProyecto.setBoolean(5, proyecto.getEstado());
                stmtProyecto.setInt(6, proyecto.tutorInterno().getId());
                stmtProyecto.setInt(7, proyecto.tutorExterno().getId());
                stmtProyecto.setString(8, "INACTIVO");

                int affectedRows = stmtProyecto.executeUpdate();
                if (affectedRows == 0) {
                    throw new RuntimeException("No se pudo insertar el proyecto.");
                }

                int idProyecto;
                try (ResultSet generatedKeys = stmtProyecto.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idProyecto = generatedKeys.getInt(1);
                    } else {
                        throw new RuntimeException("No se pudo obtener el ID del proyecto insertado.");
                    }
                }

                // 2. Insertar plan de trabajo
                int idPlan;
                try (PreparedStatement stmtPlan = conn.prepareStatement(sqlPlan, Statement.RETURN_GENERATED_KEYS)) {
                    stmtPlan.setInt(1, idProyecto);
                    stmtPlan.setInt(2, planDeTrabajo.cantHoras());

                    if (planDeTrabajo.fechaInicio() != null) {
                        stmtPlan.setDate(3, java.sql.Date.valueOf(planDeTrabajo.fechaInicio()));
                    } else {
                        stmtPlan.setNull(3, java.sql.Types.DATE);
                    }

                    if (planDeTrabajo.fechaFin() != null) {
                        stmtPlan.setDate(4, java.sql.Date.valueOf(planDeTrabajo.fechaFin()));
                    } else {
                        stmtPlan.setNull(4, java.sql.Types.DATE);
                    }

                    stmtPlan.setBoolean(5, false); // estado_aprobacion por defecto
                    stmtPlan.setString(6, planDeTrabajo.recursos());

                    affectedRows = stmtPlan.executeUpdate();
                    if (affectedRows == 0) {
                        throw new RuntimeException("No se pudo insertar el plan de trabajo.");
                    }

                    try (ResultSet generatedKeysPlan = stmtPlan.getGeneratedKeys()) {
                        if (generatedKeysPlan.next()) {
                            idPlan = generatedKeysPlan.getInt(1);
                        } else {
                            throw new RuntimeException("No se pudo obtener el ID del plan insertado.");
                        }
                    }
                }

                // 3. Insertar actividades
                try (PreparedStatement stmtActividad = conn.prepareStatement(sqlActividad)) {
                    for (Actividad actividad : planDeTrabajo.actividades()) {
                        stmtActividad.setString(1, actividad.descripcion());
                        stmtActividad.setDate(2, java.sql.Date.valueOf(actividad.fechaInicio()));
                        stmtActividad.setInt(3, actividad.horas());
                        stmtActividad.setBoolean(4, actividad.isEstado());
                        stmtActividad.setBoolean(5, actividad.requiereInforme());
                        stmtActividad.setInt(6, idPlan);

                        int insertAct = stmtActividad.executeUpdate();
                        if (insertAct == 0) {
                            throw new RuntimeException("No se pudo insertar una actividad.");
                        }
                    }
                }

                conn.commit();  // Confirmar todo
                return idProyecto;

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Error al revertir la transacción: " + rollbackEx.getMessage(), rollbackEx);
                }
                throw new RuntimeException("Error al guardar proyecto, plan o actividades: " + e.getMessage(), e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("No se pudo restaurar autoCommit", e);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión o al iniciar la transacción: " + e.getMessage(), e);
        }
    }

    @Override
    public void guardarPostulacionDeProyecto(int idEstudiante, Proyecto proyecto, PlanDeTrabajo planDeTrabajo) {
        String sqlProyecto = "INSERT INTO proyectos (nombre, descripcion, area_de_interes, ubicacion, estado, id_usuario_tutor_interno, id_usuario_tutor_externo, estado_proyecto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlPlan = "INSERT INTO planes (id_proyecto, cant_horas, fecha_inicio, fecha_fin, estado_aprobacion, recursos) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlActividad = "INSERT INTO actividades (descripcion, fecha_inicio, horas, estado, requiere_informe, id_plan, id_informe) " +
                "VALUES (?, ?, ?, ?, ?, ?, NULL)";

        String sqlActualizarEstudiante = "UPDATE estudiantes SET id_proyecto = ? WHERE id_usuario = ?";

        try (Connection conn = Conn.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtProyecto = conn.prepareStatement(sqlProyecto, Statement.RETURN_GENERATED_KEYS)
            ) {
                // 1. Insertar proyecto con estado_proyecto = 'Postulación'
                stmtProyecto.setString(1, proyecto.getNombre());
                stmtProyecto.setString(2, proyecto.getDescripcion());
                stmtProyecto.setString(3, proyecto.getAreaDeInteres());
                stmtProyecto.setString(4, proyecto.getUbicacion());
                stmtProyecto.setBoolean(5, proyecto.getEstado());
                stmtProyecto.setInt(6, proyecto.tutorInterno().getId());
                stmtProyecto.setInt(7, proyecto.tutorExterno().getId());
                stmtProyecto.setString(8, "Postulación");

                int filasProyecto = stmtProyecto.executeUpdate();
                if (filasProyecto == 0) {
                    throw new RuntimeException("No se pudo insertar el proyecto.");
                }

                int idProyecto;
                try (ResultSet keys = stmtProyecto.getGeneratedKeys()) {
                    if (keys.next()) {
                        idProyecto = keys.getInt(1);
                    } else {
                        throw new RuntimeException("No se pudo obtener el ID del proyecto insertado.");
                    }
                }

                // 2. Actualizar tabla estudiantes
                try (PreparedStatement stmtEstudiante = conn.prepareStatement(sqlActualizarEstudiante)) {
                    stmtEstudiante.setInt(1, idProyecto);
                    stmtEstudiante.setInt(2, idEstudiante);
                    int filasEstudiante = stmtEstudiante.executeUpdate();
                    if (filasEstudiante == 0) {
                        throw new RuntimeException("No se pudo actualizar el estudiante con el proyecto.");
                    }
                }

                // 3. Insertar plan de trabajo
                int idPlan;
                try (PreparedStatement stmtPlan = conn.prepareStatement(sqlPlan, Statement.RETURN_GENERATED_KEYS)) {
                    stmtPlan.setInt(1, idProyecto);
                    stmtPlan.setInt(2, planDeTrabajo.cantHoras());

                    if (planDeTrabajo.fechaInicio() != null) {
                        stmtPlan.setDate(3, java.sql.Date.valueOf(planDeTrabajo.fechaInicio()));
                    } else {
                        stmtPlan.setNull(3, java.sql.Types.DATE);
                    }

                    if (planDeTrabajo.fechaFin() != null) {
                        stmtPlan.setDate(4, java.sql.Date.valueOf(planDeTrabajo.fechaFin()));
                    } else {
                        stmtPlan.setNull(4, java.sql.Types.DATE);
                    }

                    stmtPlan.setBoolean(5, false); // estado_aprobacion por defecto
                    stmtPlan.setString(6, planDeTrabajo.recursos());

                    int filasPlan = stmtPlan.executeUpdate();
                    if (filasPlan == 0) {
                        throw new RuntimeException("No se pudo insertar el plan de trabajo.");
                    }

                    try (ResultSet planKeys = stmtPlan.getGeneratedKeys()) {
                        if (planKeys.next()) {
                            idPlan = planKeys.getInt(1);
                        } else {
                            throw new RuntimeException("No se pudo obtener el ID del plan insertado.");
                        }
                    }
                }

                // 4. Insertar actividades
                try (PreparedStatement stmtActividad = conn.prepareStatement(sqlActividad)) {
                    for (Actividad actividad : planDeTrabajo.actividades()) {
                        stmtActividad.setString(1, actividad.descripcion());
                        stmtActividad.setDate(2, java.sql.Date.valueOf(actividad.fechaInicio()));
                        stmtActividad.setInt(3, actividad.horas());
                        stmtActividad.setBoolean(4, actividad.isEstado());
                        stmtActividad.setBoolean(5, actividad.requiereInforme());
                        stmtActividad.setInt(6, idPlan);

                        int insertAct = stmtActividad.executeUpdate();
                        if (insertAct == 0) {
                            throw new RuntimeException("No se pudo insertar una actividad.");
                        }
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Error al revertir la transacción: " + rollbackEx.getMessage(), rollbackEx);
                }
                throw new RuntimeException("Error al guardar postulación: " + e.getMessage(), e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException("No se pudo restaurar autoCommit", e);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión o transacción: " + e.getMessage(), e);
        }
    }


    @Override
    public void aprobarProyecto(int idProyecto) {
        String sqlPlan = "UPDATE planes SET estado_aprobacion = 1 WHERE id_proyecto = ?";
        String sqlProyecto = "UPDATE proyectos SET estado = 1 WHERE id_proyecto = " +
                "?";
        String sqlProyecto1 = "UPDATE proyectos SET estado_proyecto = 'INACTIVO' WHERE id_proyecto = " +
                "?";


        Estudiante existeEstudiante = obtenerEstudianteAsignado(idProyecto);

        try (Connection conn = Conn.getConnection()) {

            // Actualiza el estado del plan
            try (PreparedStatement stmtPlan = conn.prepareStatement(sqlPlan)) {
                stmtPlan.setInt(1, idProyecto);
                stmtPlan.executeUpdate();
            }

            // Actualiza el estado del proyecto relacionado
            try (PreparedStatement stmtProyecto = conn.prepareStatement(sqlProyecto)) {
                stmtProyecto.setInt(1, idProyecto);
                stmtProyecto.executeUpdate();
            }
            // Actualiza el estado_proyecto del proyecto relacionado
            try (PreparedStatement stmtProyecto = conn.prepareStatement(sqlProyecto1)) {
                stmtProyecto.setInt(1, idProyecto);
                stmtProyecto.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException("Problema con la persistencia", e);
        }
    }


    public List<Proyecto> obtenerProyectosAprobados() throws SQLException {
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
    public void cargarInforme(Informe informeParcial, int idProyecto) {
        String sqlInsertInforme = "INSERT INTO informes (descripcion, fecha_entrega, tipo, valoracionInforme, estado, archivo) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdateActividad = "UPDATE actividades SET id_informe = ? WHERE id_actividad = ?";
        String sqlUpdateProyecto = "UPDATE proyectos SET id_informe_final = ? WHERE id_proyecto = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertInforme, Statement.RETURN_GENERATED_KEYS)) {

            // Insertar informe
            stmtInsert.setString(1, informeParcial.descripcion());
            stmtInsert.setDate(2, Date.valueOf(informeParcial.fechaEntrega()));
            stmtInsert.setString(3, informeParcial.tipo());
            stmtInsert.setInt(4, informeParcial.valoracionInforme());
            stmtInsert.setBoolean(5, informeParcial.estado());
            stmtInsert.setBytes(6, informeParcial.archivoEntregable());
            stmtInsert.executeUpdate();

            // Obtener ID generado
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idInforme = generatedKeys.getInt(1);

                // Actualizar según tipo
                if ("PARCIAL".equalsIgnoreCase(informeParcial.tipo())) {
                    try (PreparedStatement stmtUpdateActividad = conn.prepareStatement(sqlUpdateActividad)) {
                        stmtUpdateActividad.setInt(1, idInforme);
                        stmtUpdateActividad.setInt(2, informeParcial.id()); // idActividad
                        stmtUpdateActividad.executeUpdate();
                    }
                } else if ("FINAL".equalsIgnoreCase(informeParcial.tipo())) {
                    try (PreparedStatement stmtUpdateProyecto = conn.prepareStatement(sqlUpdateProyecto)) {
                        stmtUpdateProyecto.setInt(1, idInforme);
                        stmtUpdateProyecto.setInt(2, idProyecto);
                        stmtUpdateProyecto.executeUpdate();
                    }
                } else {
                    throw new RuntimeException("Tipo de informe no reconocido: " + informeParcial.tipo());
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
                "p.id_usuario_tutor_interno, p.id_usuario_tutor_externo, p.ubicacion, p.estado_proyecto " +
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
                    proyecto.setEstadoProyecto(rs.getString("estado_proyecto"));
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
                                boolean requiereInforme = rsAct.getBoolean("requiere_informe");

                                Actividad actividad = new Actividad(descripcion, fechaInicioAct, horas, finalizado, requiereInforme);
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

        String sql =
                "SELECT id_proyecto, nombre, descripcion, estado " +
                        "FROM proyectos " +
                        "WHERE estado = 0 AND estado_proyecto <> 'DENEGADO'";

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
                    boolean requiereInforme = rs.getBoolean("requiere_informe");


                    Actividad actividad = new Actividad(descripcion, fechaInicio, horas, finalizado, requiereInforme);
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
        String checkSql =
                "SELECT id_proyecto " +
                        "FROM estudiantes " +
                        "WHERE id_usuario = ? AND id_proyecto IS NOT NULL";

        try (Connection conn = Conn.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, idEstudiante);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                throw new SQLException("Ya has elegido o postulado un proyecto");
            }
        }

        // Asignar el estudiante al proyecto
        String updateEstudianteSql =
                "UPDATE estudiantes " +
                        "SET id_proyecto = ? " +
                        "WHERE id_usuario = ?";

        // Actualizar el estado del proyecto a "EN CURSO"
        String updateProyectoEstadoSql =
                "UPDATE proyectos " +
                        "SET estado_proyecto = 'INACTIVO' " +
                        "WHERE id_proyecto = ?";

        try (Connection conn = Conn.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transacción

            try (
                    PreparedStatement stmt1 = conn.prepareStatement(updateEstudianteSql);
                    PreparedStatement stmt2 = conn.prepareStatement(updateProyectoEstadoSql)
            ) {
                // Actualizar estudiante
                stmt1.setInt(1, idProyecto);
                stmt1.setInt(2, idEstudiante);
                int rows1 = stmt1.executeUpdate();

                // Actualizar proyecto
                stmt2.setInt(1, idProyecto);
                int rows2 = stmt2.executeUpdate();

                if (rows1 > 0 && rows2 > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true); // Restaurar auto-commit
            }
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
        String sqlValoracion = "UPDATE informes SET valoracionInforme = ? WHERE id_informe = ?";
        String sqlTipo = "SELECT tipo FROM informes WHERE id_informe = ?";
        String sqlUpdateActividad = "UPDATE actividades SET estado = TRUE WHERE id_informe = ?";
        String sqlFinalizarProyecto = "UPDATE proyectos SET estado_proyecto = 'FINALIZADO' WHERE id_informe_final = ?";

        try (Connection conn = Conn.getConnection()) {
            // Obtener el tipo del informe
            String tipoInforme = null;
            try (PreparedStatement stmtTipo = conn.prepareStatement(sqlTipo)) {
                stmtTipo.setInt(1, idInforme);
                try (ResultSet rs = stmtTipo.executeQuery()) {
                    if (rs.next()) {
                        tipoInforme = rs.getString("tipo");
                    } else {
                        throw new RuntimeException("No se encontró el informe con ID: " + idInforme);
                    }
                }
            }

            // Actualizar la valoración del informe
            try (PreparedStatement stmtValoracion = conn.prepareStatement(sqlValoracion)) {
                stmtValoracion.setInt(1, valoracion);
                stmtValoracion.setInt(2, idInforme);

                int rowsAffected = stmtValoracion.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("No se pudo actualizar la valoración del informe con ID: " + idInforme);
                }
            }

            // Actualizar según el tipo
            if ("PARCIAL".equalsIgnoreCase(tipoInforme)) {
                try (PreparedStatement stmtActividad = conn.prepareStatement(sqlUpdateActividad)) {
                    stmtActividad.setInt(1, idInforme);
                    stmtActividad.executeUpdate();
                }
            } else if ("FINAL".equalsIgnoreCase(tipoInforme)) {
                try (PreparedStatement stmtProyecto = conn.prepareStatement(sqlFinalizarProyecto)) {
                    stmtProyecto.setInt(1, idInforme);
                    stmtProyecto.executeUpdate();
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error al valorar el informe: " + ex.getMessage());
        }
    }


    @Override
    public void finalizarActividad(int idActividad) {
        String sql = "UPDATE actividades SET estado = 1 WHERE id_actividad = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idActividad);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error al finalizar la actividad");
        }
    }

    @Override
    public List<Proyecto> obtenerProyectos() {
        List<Proyecto> proyectos = new ArrayList<>();

        String sql =
                "SELECT p.id_proyecto, p.nombre, p.descripcion, p.area_de_interes, p.ubicacion, " +
                        "p.estado, p.estado_proyecto, p.id_usuario_tutor_interno, p.id_usuario_tutor_externo, " +
                        "ui.nombre AS nombre_tutor_interno, ue.nombre AS nombre_tutor_externo " +
                        "FROM proyectos p " +
                        "JOIN usuarios ui ON p.id_usuario_tutor_interno = ui.id_usuario " +
                        "JOIN usuarios ue ON p.id_usuario_tutor_externo = ue.id_usuario";


        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Proyecto resumen = new Proyecto(
                        rs.getInt("p.id_proyecto"),
                        rs.getString("p.nombre"),
                        rs.getString("p.descripcion"),
                        rs.getBoolean("p.estado"), // será false porque estado = 0
                        rs.getString("p.area_de_interes"),
                        new Tutor(rs.getInt("id_usuario_tutor_externo"), null, null, rs.getString("nombre_tutor_externo"), null, null, null),
                        new Tutor(rs.getInt("id_usuario_tutor_interno"), null, null, rs.getString("nombre_tutor_interno"), null, null, null), rs.getString("ubicacion")
                );
                resumen.setEstadoProyecto(rs.getString("p.estado_proyecto"));
                proyectos.add(resumen);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Problema de persistencia al obtener proyectos");
        }

        return proyectos;
    }

    @Override
    public void denegarProyecto(int idProyecto) {
        String sqlDenegar = "UPDATE proyectos SET estado_proyecto = 'DENEGADO' WHERE id_proyecto = ?";
        String sqlBuscarEstudiante = "SELECT id_usuario FROM estudiantes WHERE id_proyecto = ?";
        String sqlLimpiarProyecto = "UPDATE estudiantes SET id_proyecto = NULL WHERE id_proyecto = ?";

        try (Connection conn = Conn.getConnection()) {
            // 1. Denegar proyecto
            try (PreparedStatement stmt = conn.prepareStatement(sqlDenegar)) {
                stmt.setInt(1, idProyecto);
                stmt.executeUpdate();
            }

            // 2. Verificar si hay estudiante asignado
            boolean hayEstudiante;
            try (PreparedStatement stmt = conn.prepareStatement(sqlBuscarEstudiante)) {
                stmt.setInt(1, idProyecto);
                try (ResultSet rs = stmt.executeQuery()) {
                    hayEstudiante = rs.next(); // hay al menos un estudiante
                }
            }

            // 3. Si lo hay, setear id_proyecto en NULL
            if (hayEstudiante) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlLimpiarProyecto)) {
                    stmt.setInt(1, idProyecto);
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error al denegar proyecto y limpiar asignación de estudiante", ex);
        }
    }


    @Override
    public Estudiante obtenerEstudianteAsignado(int idProyecto) {
        Estudiante estudiante = null;

        String sql =
                "SELECT e.id_usuario, e.legajo, e.es_regular, e.id_proyecto, " +
                        "       u.nombre_usuario, u.contrasenia, u.nombre, u.email " +
                        "FROM   estudiantes e " +
                        "JOIN   usuarios    u ON u.id_usuario = e.id_usuario " +
                        "WHERE  e.id_proyecto = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estudiante = new Estudiante(
                            rs.getInt("id_usuario"),        // id
                            rs.getString("nombre_usuario"),    // nombreUsuario
                            rs.getString("contrasenia"),       // contrasenia
                            rs.getString("nombre"),            // nombre completo
                            rs.getString("email"),             // email
                            null,                    // rol
                            rs.getString("legajo"),            // legajo
                            rs.getBoolean("es_regular"),       // regular
                            null,  // dirección postal
                            rs.getInt("id_proyecto")           // id_proyecto
                    );
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Problema de con busqueda de estudiante del proyecto");
        }

        return estudiante; // null si no existe estudiante para el proyecto
    }

    @Override
    public boolean existeConvenio(int id, Integer idProyecto) {
        String sql = "SELECT 1 FROM convenios WHERE id_usuario = " + id + " AND id_proyecto = " + idProyecto + " LIMIT 1";

        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next(); // Retorna true si encuentra al menos una tupla
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar existencia del convenio", e);
        }
    }

    @Override
    public boolean existeInformeFinal(int idProyecto) {
        String sql = "SELECT id_informe_final FROM proyectos WHERE id_proyecto = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idInformeFinal = rs.getInt("id_informe_final");
                    return !rs.wasNull(); // true si hay informe final, false si está en NULL
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el informe final");
        }

        return false;
    }


    @Override
    public Informe obtenerInformeFinal(int idProyecto) {
        String sql = "SELECT i.id_informe, i.descripcion, i.tipo, valoracionInforme, i.archivo " +
                "FROM proyectos p " +
                "JOIN informes i ON p.id_informe_final = i.id_informe " +
                "WHERE p.id_proyecto = ?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Informe informe = new Informe(rs.getInt("id_informe"),
                            rs.getString("descripcion"),
                            rs.getString("tipo"), rs.getBytes("archivo"));
                    informe.setValoracionInforme(rs.getInt("valoracionInforme"));

                    return informe;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Podrías loguear o lanzar una excepción personalizada
        }

        return null; // No se encontró informe asociado
    }


}
