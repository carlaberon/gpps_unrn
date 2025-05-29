package model;

import java.sql.SQLException;
import java.util.List;

public interface GestorDeProyectos {

    void registrarPropuestaDeProyecto(Proyecto proyecto);


    void guardar(Proyecto proyecto) throws SQLException;

    void guardarSinEstudiante(Proyecto proyecto) throws SQLException;

    List<Proyecto> obtenerProyectos() throws SQLException;

    void registrarAsignacionTutorInterno(int idProyecto, int idTutorInterno);

    void cargarPlanDeTrabajo(PlanDeTrabajo plan, int idProyecto);

    void aprobarPlanDeTrabajo(int idPlan);

    void cargarInforme(Informe informeParcial);

    Proyecto obtenerProyecto(int idProyecto);

    PlanDeTrabajo obtenerPlan(int idProyecto);
}
