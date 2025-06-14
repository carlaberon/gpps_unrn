package model;


import java.sql.SQLException;
import java.util.List;


public class Proyectos {
    private GestorDeProyectos gestorDeProyectos;
    private ServiceInformes servicioDeVerificacionInformes;
    private ServicioDeNotificaciones servicioDeNotificaciones;


    public Proyectos(GestorDeProyectos gestorDeProyectos) {
        this.gestorDeProyectos = gestorDeProyectos;
    }

    public Proyectos(GestorDeProyectos gestorDeProyectos, ServiceInformes verificacion, ServicioDeNotificaciones servicioDeNotificaciones) {

        this.gestorDeProyectos = gestorDeProyectos;
        this.servicioDeVerificacionInformes = verificacion;
        this.servicioDeNotificaciones = servicioDeNotificaciones;
    }

    public int guardarProyectoSinEstudiante(Proyecto proyecto, PlanDeTrabajo planDeTrabajo) throws SQLException {
        return this.gestorDeProyectos.guardarProyecto(proyecto, planDeTrabajo);
    }

    public void guardarPostulacionDeProyecto(int idEstudiante, Proyecto proyecto, PlanDeTrabajo planDeTrabajo) {
        this.gestorDeProyectos.guardarPostulacionDeProyecto(idEstudiante, proyecto, planDeTrabajo);
    }


    public void propuestaDeProyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                                    Tutor tutor, Tutor docenteSupervisor, String ubicacion) {

        var proyecto = new Proyecto(id, nombre, descripcion, estado, areaDeInteres, tutor, docenteSupervisor, ubicacion);
        this.gestorDeProyectos.registrarPropuestaDeProyecto(proyecto);
    }

    public void asignarDocenteTutor(int idProyecto, int idTutorInterno) {
        this.gestorDeProyectos.registrarAsignacionTutorInterno(idProyecto, idTutorInterno);
        this.servicioDeNotificaciones.notificar(idTutorInterno, "Se le ha asignado un nuevo proyecto como tutor interno.");
    }

    public void cargarInforme(Informe informe, int idProyecto) {
        this.gestorDeProyectos.cargarInforme(informe, idProyecto);
    }

    public void valorarInformeParcial(int idInforme, int valorInforme) {
        this.servicioDeVerificacionInformes.valorarInforme(idInforme, valorInforme);
    }

    public void valorarInformeFinal(int idInforme, int valorInforme, List<Integer> idInformeParciales) {
        if (!this.servicioDeVerificacionInformes.verificarInformeParcialAprobado(idInformeParciales)) { //lista de informes parciales
            throw new RuntimeException("Los informes parciales no se encuentran evaluados. Intente en otro momento.");
        }
        this.servicioDeVerificacionInformes.valorarInforme(idInforme, valorInforme);
    }

    public List<Tutor> obtenerTutoresPorProyecto(int idProyecto) throws SQLException {
        return this.gestorDeProyectos.obtenerTutoresPorProyecto(idProyecto);
    }

    public List<Actividad> obtenerActividadesPorPlan(int idPlan) throws SQLException {
        return this.gestorDeProyectos.obtenerActividadesPorPlan(idPlan);
    }

    public void valorarInforme(int idInforme, int valoracion) {
        this.gestorDeProyectos.valorarInforme(idInforme, valoracion);
    }

}
