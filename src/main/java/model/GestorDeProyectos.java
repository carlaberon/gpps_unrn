package model;

import java.sql.SQLException;
import java.util.List;

public interface GestorDeProyectos {

    void registrarPropuestaDeProyecto(Proyecto proyecto);

    int guardarProyecto(Proyecto proyecto, PlanDeTrabajo planDeTrabajo);

    List<Proyecto> obtenerProyectosAprobados() throws SQLException;

    List<Proyecto> listarProyectosRelacionados(int idUsuario);

    void registrarAsignacionTutorInterno(int idProyecto, int idTutorInterno);

    void aprobarProyecto(int idProyecto);

    void cargarInforme(Informe informeParcial);

    Proyecto obtenerProyecto(int idProyecto);

    PlanDeTrabajo obtenerPlan(int idProyecto);

    List<Proyecto> obtenerProyectosSinAprobar();

    void notificarComentarioDenegacion(String comentario);

    List<Tutor> obtenerTutoresPorProyecto(int idProyecto) throws SQLException;

    List<Actividad> obtenerActividadesPorPlan(int idPlan) throws SQLException;

    boolean asignarEstudianteAProyecto(int idEstudiante, int idProyecto) throws SQLException;

    Informe obtenerInforme(int idInforme);

    void valorarInforme(int idInforme, int valoracion);

    void finalizarActividad(int idActividad);

    List<Proyecto> obtenerProyectos();

    void denegarProyecto(int idProyecto);

    Estudiante obtenerEstudianteAsignado(int idProyecto);

    boolean existeConvenio(int id, Integer idProyecto);
}
