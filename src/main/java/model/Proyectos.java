package model;

public class Proyectos {
    private GestorDeProyectos gestorDeProyectos;
    private ServiceInformes servicioDeVerificacionInformes;

    public Proyectos(GestorDeProyectos gestorDeProyectos, ServiceInformes verificacion) {
        this.gestorDeProyectos = gestorDeProyectos;
        this.servicioDeVerificacionInformes = verificacion;
    }

    public void propuestaDeProyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                                    Estudiante estudiante, Tutor tutor, Tutor docenteSupervisor) {
        var proyecto = new Proyecto(id, nombre, descripcion, estado, areaDeInteres, estudiante, tutor, docenteSupervisor);
        this.gestorDeProyectos.registrarPropuestaDeProyecto(proyecto);
        //registra el proyecto
    }

    public void asignarDocenteTutor(int idProyecto, int idDocente, int idTutor) {
        this.gestorDeProyectos.registrarAsignacionDocenteTutor(idProyecto, idDocente, idTutor);
    }
    public void cargarInformeParcial(int idInformeParcial, String descripcionInformeParcial){

        var informeParcial = new Informe(idInformeParcial, descripcionInformeParcial);
        this.gestorDeProyectos.cargarInformeParcial(informeParcial);
    }
    public void cargarInformeFinal(int idInformeParcial, int idInformeFinal,String descripcionInformeFinal){
        if (! this.servicioDeVerificacionInformes.verificarInformeParcialAprobado(idInformeParcial)){
            throw new RuntimeException("El informe parcial no se encuentra aprobado. Intente en otro momento.");
        }
        var informeFinal = new Informe(idInformeFinal, descripcionInformeFinal);
        this.gestorDeProyectos.cargarInformeFinal(informeFinal);
    }
    public void valorarInforme(int idInforme, int valorInforme){
        var informe = servicioDeVerificacionInformes.obtenerInforme(idInforme);
        this.servicioDeVerificacionInformes.valorarInforme(informe, valorInforme);
    }
}
