package database;

import model.*;

import java.sql.*;
//el import...dependencia

public class ServicioDePersistenciaGestionProyectos implements GestorDeProyectos {
    private Connection conn;

    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {
        //aca implemento
        //ahora muestro cómo hacer las pruebas sin tener la implementacion

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
    public void cargarInformeParcial(Informe informeParcial) {
        //cargar informeParcial
    }

    @Override
    public void cargarInformeFinal(Informe informeFinal) {
        //cargar informeFinal
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
        String sql = "UPDATE planes SET estado_aprobacion = ? WHERE id_plan = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setBoolean(1, true);
            statement.setInt(2, idPlan);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Problema con la persistencia");
        }
    }


}
