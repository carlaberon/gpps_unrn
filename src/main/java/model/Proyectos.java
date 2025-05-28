package model;


import java.sql.SQLException;
import java.util.List;


public class Proyectos {
    private GestorDeProyectos gestorDeProyectos;
    private ServiceInformes servicioDeVerificacionInformes;


    public Proyectos(GestorDeProyectos gestorDeProyectos) {
        this.gestorDeProyectos = gestorDeProyectos;
    }

    public Proyectos(GestorDeProyectos gestorDeProyectos, ServiceInformes verificacion) {

        this.gestorDeProyectos = gestorDeProyectos;
        this.servicioDeVerificacionInformes = verificacion;
    }


//    public void propuestaDeProyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
//                                    Estudiante estudiante, Tutor tutor, Tutor docenteSupervisor) {
//        var proyecto = new Proyecto(id, nombre, descripcion, estado, areaDeInteres, estudiante, tutor, docenteSupervisor);
//        this.gestorDeProyectos.registrarPropuestaDeProyecto(proyecto);
//        //registra el proyecto
//    }

    public void guardarProyecto(Proyecto proyecto) throws SQLException {
        //var proyecto = new Proyecto(id, nombre, descripcion, estado, areaDeInteres, estudiante, tutor, docenteSupervisor);
        this.gestorDeProyectos.guardar(proyecto);
    }

    public void guardarProyectoSinEstudiante(Proyecto proyecto) throws SQLException {
        this.gestorDeProyectos.guardarSinEstudiante(proyecto);
    }

    public void propuestaDeProyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                                    Estudiante estudiante, Tutor tutor, Tutor docenteSupervisor, String ubicacion) {
        var proyecto = new Proyecto(id, nombre, descripcion, estado, areaDeInteres, tutor, docenteSupervisor, ubicacion);
        this.gestorDeProyectos.registrarPropuestaDeProyecto(proyecto);
        //registra el proyecto
    }

    public void asignarDocenteTutor(int idProyecto, int idTutorInterno) {
        this.gestorDeProyectos.registrarAsignacionTutorInterno(idProyecto, idTutorInterno);
    }

    public void cargarInforme(int idInformeParcial, String descripcionInformeParcial, String tipo) {

        var informeParcial = new Informe(idInformeParcial, descripcionInformeParcial, tipo);
        this.gestorDeProyectos.cargarInforme(informeParcial);
    }

    public void cargarInformeFinal(String descripcionInformeFinal, String tipo, List<Integer> idInformesParciales, int idInformeFinal) {
        if (!this.servicioDeVerificacionInformes.verificarInformeParcialAprobado(idInformesParciales)) { //lista de informes parciales
            throw new RuntimeException("El informe parcial no se encuentra aprobado. Intente en otro momento.");
        }
        var informeFinal = new Informe(idInformeFinal, descripcionInformeFinal, tipo);
        this.gestorDeProyectos.cargarInforme(informeFinal);
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

}
