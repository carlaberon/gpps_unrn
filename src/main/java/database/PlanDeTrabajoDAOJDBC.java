//package database;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import model.Actividad;
//import model.PlanDAO;
//import model.PlanDeTrabajo;
//
//public class PlanDeTrabajoDAOJDBC implements PlanDAO {
//	public PlanDeTrabajo obtenerPorProyecto(int idProyecto) throws SQLException {
//	    String sqlPlan = "SELECT * FROM planes WHERE id_proyecto = ?";
//	    String sqlActividades = "SELECT * FROM actividades WHERE id_plan = ?";
//
//	    try (Connection conn = Conn.getConnection();
//	         PreparedStatement stmtPlan = conn.prepareStatement(sqlPlan)) {
//
//	        stmtPlan.setInt(1, idProyecto);
//	        ResultSet rsPlan = stmtPlan.executeQuery();
//
//	        if (rsPlan.next()) {
//	            int idPlan = rsPlan.getInt("id_plan");
//	            PlanDeTrabajo plan = new PlanDeTrabajo(
//	                idPlan,
//	                idProyecto,
//	                rsPlan.getInt("cant_horas"),
//	                rsPlan.getDate("fecha_inicio").toLocalDate(),
//	                rsPlan.getDate("fecha_fin").toLocalDate()
//	            );
//	            try (PreparedStatement stmtActs = conn.prepareStatement(sqlActividades)) {
//	                stmtActs.setInt(1, idPlan);
//	                ResultSet rsActs = stmtActs.executeQuery();
//
//	                while (rsActs.next()) {
//	                	Actividad actividad = new SinInforme(
//
//	                        rsActs.getString("descripcion"),
//	                        rsActs.getDate("fecha_inicio").toLocalDate(),
//	                        rsActs.getInt("horas"),
//	                        "completada".equalsIgnoreCase(rsActs.getString("estado"))
//	                    );
//	                	actividad.setIdActividad(rsActs.getInt("id_actividad"));
//	                    plan.agregarActividad(actividad);
//	                }
//	            }
//
//	            return plan;
//	        } else {
//	            throw new SQLException("No se encontró plan para el proyecto " + idProyecto);
//	        }
//	    }
//	}
//}
